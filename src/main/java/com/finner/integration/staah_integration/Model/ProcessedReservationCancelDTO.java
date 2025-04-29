package com.finner.integration.staah_integration.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor        // Generates default no-args constructor
@AllArgsConstructor
public class ProcessedReservationCancelDTO {
    @JsonProperty("parent_property")
    private String parent_property;

    @JsonProperty("total_amount")
    private Double total_amount;

    @JsonProperty("tax_amount")
    private Double tax_amount;

    @JsonProperty("OTA_Channel")
    private String OTA_Channel;

    @JsonProperty("checkIn_date")
    private String checkIn_date;

    @JsonProperty("checkOut_date")
    private String checkOut_date;

    @JsonProperty("room_category")
    private String room_category;

    @JsonProperty("ota_booking_id")
    private String ota_booking_id;

    @JsonProperty("ota_property_id")
    private String ota_property_id;

    @JsonProperty("ota_room_id")
    private String ota_room_id;

    @JsonProperty("ota_booking_rev")
    private String ota_booking_rev;

}
