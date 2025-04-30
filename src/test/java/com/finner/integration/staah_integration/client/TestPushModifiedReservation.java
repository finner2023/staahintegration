package com.finner.integration.staah_integration.client;


import com.finner.integration.staah_integration.Model.*;
import com.finner.integration.staah_integration.Service.ReservationProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest
public class TestPushModifiedReservation {

    @Autowired
    private PmsApiClient pmsApiClient;

    @Autowired
    private ReservationProcessor processor;

    @Test
    public void pushDummyModifiedReservationToPms() {
        // ✅ Dummy Customer
        Customer dummyCustomer = new Customer();
        dummyCustomer.setFirst_name("Jane");
        dummyCustomer.setLast_name("Smith");
        dummyCustomer.setEmail("jane.smith@example.com");
        dummyCustomer.setTelephone("9123456789"); // Still String
        dummyCustomer.setCountrycode("US");
        dummyCustomer.setAddress("456 Test Avenue");

        // ✅ Dummy Room
        Room dummyRoom = new Room();
        dummyRoom.setArrival_date("2025-05-01");
        dummyRoom.setDeparture_date("2025-05-05");
        dummyRoom.setRoomType("1736417511030x624046131568967700"); // Room Category ID
        dummyRoom.setTotaltax("100.0"); // As String
        dummyRoom.setTotalprice("8000.0"); // As String
        dummyRoom.setRoomreservation_id("TXN456");
        dummyRoom.setNumberofguests("3");  // Will be parsed to Integer

        // ✅ Dummy Price
        Price dummyPrice = new Price();
        dummyPrice.setMealplan("Breakfast Included");
        dummyPrice.setMealplan_id("Breakfast");

        // ✅ Dummy Affiliation
        Affiliation dummyAffiliation = new Affiliation();
        dummyAffiliation.setPos("BookingCom");

        // ✅ Dummy Reservation
        StaahReservation dummyReservation = new StaahReservation();
        dummyReservation.setId("RES456");
        dummyReservation.setBooked_at("2025-04-28");
        dummyReservation.setCommissionamount("8000.0"); // Will be parsed
        dummyReservation.setPaymenttype("Hotel Collect");
        dummyReservation.setHotel_id("1563055671");
        dummyReservation.setCurrencycode("USD");
        dummyReservation.setReservation_notif_id("NOTIF456");
        dummyReservation.setStatus("modified");
        dummyReservation.setCustomer(dummyCustomer);
        dummyReservation.setRooms(Collections.singletonList(dummyRoom));
        dummyReservation.setPrice(Collections.singletonList(dummyPrice));
        dummyReservation.setAffiliation(dummyAffiliation);

        // ✅ Set Modification Reason
        String modificationReason = "Guest upgraded room";

        // ✅ Process using your new mapping method
        ProcessedReservationModifiedDTO modifiedDto = ReservationProcessor.processModified(dummyReservation, dummyRoom, modificationReason);

        System.out.println("📦 Generated Modified DTO:\n" + modifiedDto);

        // ✅ Push to PMS
        Mono<Void> result = pmsApiClient.pushModification(modifiedDto);
        result
                .doOnSuccess(unused -> System.out.println("✅ Successfully pushed modified dummy reservation to PMS."))
                .doOnError(error -> System.err.println("❌ Error pushing modified dummy reservation: " + error.getMessage()))
                .block(); // Blocking for test only

        System.out.println("✅ Modified Reservation Test completed.");
    }
}
