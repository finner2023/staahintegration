package com.finner.integration.staah_integration.client;

import com.finner.integration.staah_integration.Model.GroupReservationDTO;
import com.finner.integration.staah_integration.Model.ProcessedReservationCancelDTO;
import com.finner.integration.staah_integration.Model.ProcessedReservationModifiedDTO;
import com.finner.integration.staah_integration.Model.ProcessedReservationNewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
@Slf4j
@Component
public class PmsApiClient {

    private final WebClient webClient;

    @Value("${pms.new.endpoint}")
    private String pmsNewEndpoint;

    @Value("${pms.modified.endpoint}")
    private String pmsModifiedEndpoint;

    @Value("${pms.cancel.endpoint}")
    private String pmsCancelEndpoint;

    @Value("${pms.bearer.token}")
    private String bearerToken;

    @Value("${pms.group.endpoint}")
    private String pmsGroupEndpoint;

    public PmsApiClient(@Qualifier("pmsWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> pushReservation(ProcessedReservationNewDTO dto) {

        return webClient.post()
                .uri(pmsNewEndpoint)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .bodyValue(dto)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("❌ Client/Server error: {}", errorBody);
                                    return Mono.error(new RuntimeException("Client error: " + errorBody));
                                }))

                .toBodilessEntity()
                .then();

    }

    public Mono<Void> pushModification(ProcessedReservationModifiedDTO dto) {
        return webClient.post()
                .uri(pmsModifiedEndpoint)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    public Mono<Void> pushCancellation(ProcessedReservationCancelDTO dto) {
        return webClient.post()
                .uri(pmsCancelEndpoint)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
    public Mono<String> sendGroupReservation(GroupReservationDTO dto) {
        return webClient.post()
                .uri(pmsGroupEndpoint)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(String.class); // This assumes the API returns the group_reservation_id as a plain string
    }

}
