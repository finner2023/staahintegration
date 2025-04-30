package com.finner.integration.staah_integration.client;

import com.finner.integration.staah_integration.Model.Customer;
import com.finner.integration.staah_integration.Model.ProcessedReservationCancelDTO;
import com.finner.integration.staah_integration.Model.Room;
import com.finner.integration.staah_integration.Model.StaahReservation;
import com.finner.integration.staah_integration.Service.ReservationProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest
public class TestPushCancelReservation {

    @Autowired
    private PmsApiClient pmsApiClient;

    @Autowired
    private ReservationProcessor processor;

    @Test
    public void pushDummyCancelledReservationToPms() {
        // ✅ Dummy Customer
        Customer dummyCustomer = new Customer();
        dummyCustomer.setFirst_name("Mike");
        dummyCustomer.setLast_name("Johnson");
        dummyCustomer.setEmail("mike.johnson@example.com");
        dummyCustomer.setTelephone("9876543210");
        dummyCustomer.setCountrycode("US");
        dummyCustomer.setAddress("789 Test Street");

        // ✅ Dummy Room
        Room dummyRoom = new Room();
        dummyRoom.setArrival_date("2025-06-01");
        dummyRoom.setDeparture_date("2025-06-05");
        dummyRoom.setRoomType("1736417511030x624046131568967700");
        dummyRoom.setTotaltax("0.0");
        dummyRoom.setTotalprice("0.0");
        dummyRoom.setRoomreservation_id("TXN789");
        dummyRoom.setNumberofguests("2");

        // ✅ Dummy Reservation
        StaahReservation dummyReservation = new StaahReservation();
        dummyReservation.setId("RES789");
        dummyReservation.setBooked_at("2025-05-28");
        dummyReservation.setCommissionamount("0.0");
        dummyReservation.setPaymenttype("Hotel Collect");
        dummyReservation.setHotel_id("");
        dummyReservation.setCurrencycode("USD");
        dummyReservation.setReservation_notif_id("NOTIF789");
        dummyReservation.setStatus("cancel"); // Important
        dummyReservation.setCustomer(dummyCustomer);
        dummyReservation.setRooms(Collections.singletonList(dummyRoom));

        // ✅ Set Cancel Reason
        String cancelReason = "Guest requested cancellation";

        // ✅ Process using your new mapping method
        ProcessedReservationCancelDTO cancelDto = ReservationProcessor.processCancel(dummyReservation, dummyRoom, cancelReason);

        System.out.println("📦 Generated Cancel DTO:\n" + cancelDto);

        // ✅ Push to PMS
        Mono<Void> result = pmsApiClient.pushCancellation(cancelDto);
        result
                .doOnSuccess(unused -> System.out.println("✅ Successfully pushed cancelled dummy reservation to PMS."))
                .doOnError(error -> System.err.println("❌ Error pushing cancelled dummy reservation: " + error.getMessage()))
                .block(); // Blocking for test only

        System.out.println("✅ Cancelled Reservation Test completed.");
    }
}
