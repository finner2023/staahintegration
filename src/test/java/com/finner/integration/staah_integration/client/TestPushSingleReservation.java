package com.finner.integration.staah_integration.client;

import com.finner.integration.staah_integration.Model.Customer;
import com.finner.integration.staah_integration.Model.ProcessedReservationNewDTO;
import com.finner.integration.staah_integration.Model.Room;
import com.finner.integration.staah_integration.Model.StaahReservation;
import com.finner.integration.staah_integration.Service.ReservationProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest
public class TestPushSingleReservation {

    @Autowired
    private PmsApiClient pmsApiClient;

    @Autowired
    private ReservationProcessor processor;

    @Test
    public void pushDummyReservationToPms() {
        // ✅ Dummy Customer
        Customer dummyCustomer = new Customer();
        dummyCustomer.setFirst_name("John");
        dummyCustomer.setLast_name("Doe");
        dummyCustomer.setEmail("john.doe@example.com");
        dummyCustomer.setTelephone("9876543210"); // Still String; will be parsed to Long in processor
        dummyCustomer.setCountrycode("IN");
        dummyCustomer.setAddress("123 Test Lane");

        // ✅ Dummy Room
        Room dummyRoom = new Room();
        dummyRoom.setArrival_date("2025-04-10");
        dummyRoom.setDeparture_date("2025-04-12");
        dummyRoom.setRoomType("1736417511030x624046131568967700"); // Room Category ID from your payload
        dummyRoom.setTotaltax("50.0");  // As String; will be parsed to Double
        dummyRoom.setTotalprice("5000.0"); // As String; will be parsed to Double
        dummyRoom.setRoomreservation_id("TXN123");
        dummyRoom.setNumberofguests("2");  // Will be parsed to Integer

        // ✅ Dummy Reservation
        StaahReservation dummyReservation = new StaahReservation();
        dummyReservation.setId("RES123");
        dummyReservation.setBooked_at("2025-04-08");
        dummyReservation.setCommissionamount("5000.0"); // Will be parsed to Double
        dummyReservation.setPaymenttype("Hotel Collect");
        dummyReservation.setHotel_id("1563055671");
        dummyReservation.setCurrencycode("INR");
        dummyReservation.setReservation_notif_id("NOTIF123");
        dummyReservation.setStatus("new");
        dummyReservation.setCustomer(dummyCustomer);
        dummyReservation.setRooms(Collections.singletonList(dummyRoom));

        // ✅ Process using reservation processor
        ProcessedReservationNewDTO dto = processor.processNew(dummyReservation, dummyRoom);
        dto.setOTA_Channel("BookingCom"); // ✅ Set valid OTA option set
        dto.setMeal_plan_optionset("Breakfast"); // ✅ Same value as meal_plan
        dto.setOta_booking_id("OTABOOKINGID"); // ✅ Set dummy booking ID if needed

        System.out.println("📦 Generated DTO:\n" + dto);

        // ✅ Push to PMS
        Mono<Void> result = pmsApiClient.pushReservation(dto);
        result
                .doOnSuccess(unused -> System.out.println("✅ Successfully pushed dummy reservation to PMS."))
                .doOnError(error -> System.err.println("❌ Error pushing dummy reservation: " + error.getMessage()))
                .block(); // Blocking for test only

        System.out.println("✅ Test completed.");
    }
}
