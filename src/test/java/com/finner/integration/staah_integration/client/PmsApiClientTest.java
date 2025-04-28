//package com.finner.integration.staah_integration.client;
//
//import com.finner.integration.staah_integration.Model.ProcessedReservationNewDTO;
//import com.finner.integration.staah_integration.client.PmsApiClient;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//@SpringBootTest
//public class PmsApiClientTest {
//
//    @Autowired
//    private PmsApiClient pmsApiClient;
//
//    @Test
//    public void testPushReservation() {
//        ProcessedReservationNewDTO dummyDto = ProcessedReservationNewDTO.builder()
//                .guest_name("John Doe")
//                .guest_email("john.doe@example.com")
//                .guestphone("1234567890")
//                .checkin_date("2025-04-10")
//                .checkOutdate("2025-04-12")
//                .room_category("Standard")
//                .tax_amount(50.0)
//                .meal_plan("Breakfast")
//                .ota_property_id("TEST_PROP")
//                .currency("INR")
//                .no_of_guest(2)
//                .phone_countryC("IN")
//                .guest_address("Test Address")
//                .Natonality("IN")
//                .ota_booking_rev("REV123")
//                .totalamount("5000")
//                .pms_transaction_id("TXN123")
//                .pay_hotelOTA("Hotel Collect")
//                .OTAChannel("OTA_Channel_Ex")
//                .otabooking_id("BOOK123")
//                .meal_plan_optic("Breakfast_Optic")
//                .parent_property("1563055671")
//                .build();
//
//        Mono<Void> result = pmsApiClient.pushReservation(dummyDto);
//
//        StepVerifier.create(result)
//                .expectComplete()
//                .verify();
//    }
//}
