package com.finner.integration.staah_integration.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finner.integration.staah_integration.Model.ReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class StaahApiClient {
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${staah.reservations.endpoint:https://connect-sandbox.su-api.com/SUAPI/jservice/Reservation}")
    private String staahReservationEndpoint;

    @Value("${staah.username}")
    private String username;

    @Value("${staah.password}")
    private String password;

    @Value("${staah.appId}")
    private String appId;
    @Value("${staah.api.key}")
    private String key;
    private final WebClient webClient;

    public StaahApiClient(@Qualifier("staahWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ReservationResponse> fetchReservations() {
        String credentials = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> requestBody = Map.of(
                   "hotelid","1744883633"
//                "fromDate", "2024-03-24",
//                "toDate", "2024-03-25"
        );

        System.out.println("🔐 Encoded credentials: " + encodedAuth);
        System.out.println("📦 App ID: " + appId);

        return webClient.post()
                .uri(staahReservationEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + key)
                .header("app-id", appId)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    System.err.println("❌ STAAH 4xx error body: " + error);
                                    return Mono.error(new RuntimeException("Client error: " + error));
                                })
                )
                .onStatus(status -> status.is5xxServerError(), response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    System.err.println("❌ STAAH 5xx error body: " + error);
                                    return Mono.error(new RuntimeException("Server error: " + error));
                                })
                )
                .bodyToMono(String.class)
                .doOnNext(json -> {
                    System.out.println("🧾 Raw JSON response:");
                    System.out.println(json);
                })
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, ReservationResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });


    }
    public Mono<Void> acknowledgeReservation(String hotelId, String reservationNotifId) {
        Map<String, Object> payload = Map.of(
                "hotelid", hotelId,
                "reservation_notif", Map.of("reservation_notif_id", List.of(reservationNotifId))
        );

        return webClient.post()
                .uri("https://connect-sandbox.su-api.com/SUAPI/jservice/Reservation_notif")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + key) // Use your base64 encoded credentials
                .header("app-id", appId)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .then();
    }


}
