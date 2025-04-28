package com.finner.integration.staah_integration.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor        // Generates default no-args constructor
@AllArgsConstructor
public class StaahReservation {
    private String booked_at;
    private String commissionamount;
    private String currencycode;
    private String paymenttype;
    private String hotel_id;
    private String hotel_name;
    private Customer customer;
    private List<Room> rooms;
    private Affiliation affiliation;
    private String totalprice;
    private String totaltax;
    private String status;
    private String id;
    private String reservation_notif_id;
    private String channel_booking_id; // Needed for ota_booking_id
    private String source; // Needed for OTA_Channel
    private List<Price> price;
}
