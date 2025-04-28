package com.finner.integration.staah_integration.Service;

import com.finner.integration.staah_integration.client.PmsApiClient;
import com.finner.integration.staah_integration.client.StaahApiClient;
import com.finner.integration.staah_integration.Model.Room;
import com.finner.integration.staah_integration.Model.StaahReservation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.finner.integration.staah_integration.util.HashUtils;
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
                          ReservationProcessor reservationProcessor, ReservationHashCacheService hashCacheService,
                          PmsApiClient pmsApiClient) {
        this.staahApiClient = staahApiClient;
        this.cacheService = cacheService;
        this.reservationProcessor = reservationProcessor;
        this.pmsApiClient = pmsApiClient;
        this.hashCacheService = hashCacheService;
    }

    // Every 5 minutes
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void pollStaahAndProcess() {
        staahApiClient.fetchReservations()
                .flatMapMany(response -> {
                    String newHash = HashUtils.computeHash(response.toString());
                    if (newHash.equals(lastReservationsHash)) {
                        System.out.println("🔁 No changes in reservation data. Skipping processing.");
                        return Flux.empty();
                    }
                    lastReservationsHash = newHash;
                    return Flux.fromIterable(response.getReservations());
                })
                .flatMap(reservation -> {
                    return Flux.fromIterable(reservation.getRooms())
                            .filter(room -> {
                                String reservationId = reservation.getId();
                                return !cacheService.isAlreadyProcessed(room.getRoomreservation_id()) &&
                                        !hashCacheService.isSameReservation(reservationId, reservation);
                            })
                            .flatMap(room -> {
                                String resStatus = reservation.getStatus();
                                String roomStatus = room.getRoomstaystatus();

                                Mono<Void> resultMono;
                                if ("cancel".equalsIgnoreCase(resStatus) || "cancelled".equalsIgnoreCase(roomStatus)) {
                                    var cancelDto = reservationProcessor.processCancel(reservation, room, "Cancellation triggered by STAAH");
                                    resultMono = pmsApiClient.pushCancellation(cancelDto);
                                } else if ("modified".equalsIgnoreCase(resStatus) || "modified".equalsIgnoreCase(roomStatus)) {
                                    var modDto = reservationProcessor.processModified(reservation, room, "Reservation modified");
                                    resultMono = pmsApiClient.pushModification(modDto);
                                } else if ("new".equalsIgnoreCase(resStatus) || "new".equalsIgnoreCase(roomStatus)) {
                                    var newDto = reservationProcessor.processNew(reservation, room);
                                    resultMono = pmsApiClient.pushReservation(newDto);
                                } else {
                                    System.err.println("Unknown status for room " + room.getRoomreservation_id() +
                                            ": " + resStatus + "/" + roomStatus);
                                    return Mono.empty();
                                }

                                return resultMono.doOnSuccess(unused -> {
                                    cacheService.markAsProcessed(room.getRoomreservation_id(), reservation);

                                    staahApiClient.acknowledgeReservation(
                                            reservation.getHotel_id(),
                                            reservation.getReservation_notif_id()

                                    ).subscribe();
                                });
                            });
                });
    }
}

