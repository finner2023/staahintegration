package com.finner.integration.staah_integration.Service;

import com.finner.integration.staah_integration.client.PmsApiClient;
import com.finner.integration.staah_integration.client.StaahApiClient;
import com.finner.integration.staah_integration.Model.Room;
import com.finner.integration.staah_integration.Model.StaahReservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.finner.integration.staah_integration.util.HashUtils;

@Slf4j
@Service
public class PollingService {

    private final StaahApiClient staahApiClient;
    private final ReservationCacheService cacheService;
    private final ReservationProcessor reservationProcessor;
    private final PmsApiClient pmsApiClient;
    private final ReservationHashCacheService hashCacheService;
    private String lastReservationsHash = null;

    public PollingService(StaahApiClient staahApiClient,
                          ReservationCacheService cacheService,
                          ReservationProcessor reservationProcessor,
                          ReservationHashCacheService hashCacheService,
                          PmsApiClient pmsApiClient) {
        this.staahApiClient = staahApiClient;
        this.cacheService = cacheService;
        this.reservationProcessor = reservationProcessor;
        this.pmsApiClient = pmsApiClient;
        this.hashCacheService = hashCacheService;
    }

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void pollStaahAndProcess() {
        log.info("🔄 Starting reservation polling from STAAH...");

        staahApiClient.fetchReservations()
                .flatMapMany(response -> {
                    String newHash = HashUtils.computeHash(response.toString());
                    if (newHash.equals(lastReservationsHash)) {
                        log.info("🔁 No change detected in fetched reservations. Skipping processing.");
                        return Flux.empty();
                    }
                    log.info("🆕 New reservation data detected. Processing started...");
                    lastReservationsHash = newHash;
                    return Flux.fromIterable(response.getReservations());
                })
                .flatMap(reservation -> {
                    return Flux.fromIterable(reservation.getRooms())
                            .filter(room -> {
                                String reservationId = reservation.getId();
                                boolean alreadyProcessed = cacheService.isAlreadyProcessed(room.getRoomreservation_id());
                                boolean sameReservation = hashCacheService.isSameReservation(reservationId, reservation);

                                if (alreadyProcessed) {
                                    log.debug("✅ Room [{}] already processed. Skipping.", room.getRoomreservation_id());
                                }
                                if (sameReservation) {
                                    log.debug("✅ Reservation [{}] is identical to previous. Skipping.", reservationId);
                                }
                                return !alreadyProcessed && !sameReservation;
                            })
                            .flatMap(room -> {
                                String resStatus = reservation.getStatus();
                                String roomStatus = room.getRoomstaystatus();

                                log.info("➡️  Processing Room [{}] with status Reservation=[{}], Room=[{}]",
                                        room.getRoomreservation_id(), resStatus, roomStatus);

                                Mono<Void> resultMono;
                                if ("cancel".equalsIgnoreCase(resStatus) || "cancelled".equalsIgnoreCase(roomStatus)) {
                                    var cancelDto = ReservationProcessor.processCancel(reservation, room, "Cancellation triggered by STAAH");
                                    log.info("🗑️  Sending cancel payload for Room [{}]", room.getRoomreservation_id());
                                    resultMono = pmsApiClient.pushCancellation(cancelDto);
                                } else if ("modified".equalsIgnoreCase(resStatus) || "modified".equalsIgnoreCase(roomStatus)) {
                                    var modDto = ReservationProcessor.processModified(reservation, room, "Reservation modified");
                                    log.info("✏️  Sending modification payload for Room [{}]", room.getRoomreservation_id());
                                    resultMono = pmsApiClient.pushModification(modDto);
                                } else if ("new".equalsIgnoreCase(resStatus) || "new".equalsIgnoreCase(roomStatus)) {
                                    var newDto = reservationProcessor.processNew(reservation, room);
                                    log.info("🆕 Sending new reservation payload for Room [{}]", room.getRoomreservation_id());
                                    resultMono = pmsApiClient.pushReservation(newDto);
                                } else {
                                    log.warn("⚠️ Unknown status [{}]/[{}] for Room [{}]. Skipping.", resStatus, roomStatus, room.getRoomreservation_id());
                                    return Mono.empty();
                                }

                                return resultMono
                                        .doOnSuccess(unused -> {
                                            log.info("✅ Successfully pushed Room [{}] to PMS.", room.getRoomreservation_id());
                                            cacheService.markAsProcessed(room.getRoomreservation_id(), reservation);
                                            staahApiClient.acknowledgeReservation(
                                                    reservation.getHotel_id(),
                                                    reservation.getReservation_notif_id()
                                            ).subscribe();
                                        })
                                        .doOnError(e -> {
                                            log.error("❌ Error while pushing Room [{}] to PMS: {}", room.getRoomreservation_id(), e.getMessage(), e);
                                        });
                            });
                })
                .doOnError(e -> {
                    log.error("❌ Error during polling or processing: {}", e.getMessage(), e);
                })
                .doOnComplete(() -> {
                    log.info("✅ Finished one full polling cycle.");
                })
                .subscribe();
    }
}
