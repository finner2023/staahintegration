//package com.finner.integration.staah_integration.client;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.client.MockRestServiceServer;
//
//@RestClientTest(StaahApiClient.class)
//@TestPropertySource(properties = {
//        "staaah.reservations.endpoint=https://connect-sandbox.su-api.com/SUAPI/jservice/Reservation",
//        "staaah.username=support@thefinner.com",
//        "staaah.password=Finner@2025",
//        "staaah.appid=ZmlubmVyLnN1aXNzdS5jb20="
//})
//public class STAAHReservationClientRestTest {
//
//    @Autowired
//    private StaahApiClient staaReservationClient;
//
//    @Autowired
//    private MockRestServiceServer server;
//
//    @BeforeEach
//    public void setup() {
//        // The MockRestServiceServer is automatically configured for the RestTemplate
//    }
//
//    @Test
//    public void testPollReservations_RestClientTest() {
//        String sampleResponse = "{\"reservations\": [{\"reservationId\": \"RES123456\"}]}";
//
//        // Expect a GET request to the configured endpoint
//        server.expect(requestTo("https://connect-sandbox.su-api.com/SUAPI/jservice/Reservation"))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withSuccess(sampleResponse, MediaType.APPLICATION_JSON));
//
//        ResponseEntity<String> response = staaReservationClient.pollReservations();
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(sampleResponse);
//    }
//}