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
public class Room {
    private String arrival_date;
    private String departure_date;
    private String guest_name;
    private String roomreservation_id;
    private String numberofguests;
    private String roomstaystatus;
    private String totalprice;
    private String totaltax;
    private String roomType;
    private List<Price> price;
    private String id;
    private List<Addon> addons;
}
