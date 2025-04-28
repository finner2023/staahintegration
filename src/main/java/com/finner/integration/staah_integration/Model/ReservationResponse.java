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
public class ReservationResponse {
    private List<StaahReservation> reservations;

    public List<StaahReservation> getReservations() {
        return reservations;
    }
}
