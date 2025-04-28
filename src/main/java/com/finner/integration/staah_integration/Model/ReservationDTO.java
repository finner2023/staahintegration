package com.finner.integration.staah_integration.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor        // Generates default no-args constructor
@AllArgsConstructor

public class ReservationDTO {
    private String id;
    private String guestName;
    private String checkIn;
    private String checkOut;
    private String roomType;
    private double totalPrice;
    private String createdAt;


}
