package com.finner.integration.staah_integration.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReservationDTO {
    @JsonProperty("guest_name") private String guest_name;
    @JsonProperty("guest_email") private String guest_email;
    @JsonProperty("parent_property") private String parent_property;
    @JsonProperty("total_amount") private Double total_amount;
    @JsonProperty("tax_amount") private Double tax_amount;
    @JsonProperty("OTA_Channel") private String OTA_Channel;
    @JsonProperty("guest_phone") private Long guest_phone;
    @JsonProperty("checkIn_date") private String checkIn_date;
    @JsonProperty("checkOut_date") private String checkOut_date;
    @JsonProperty("phone_countryC") private String phone_countryC;
    @JsonProperty("no_of_guest") private Integer no_of_guest;
    @JsonProperty("guest_address") private String guest_address;
    @JsonProperty("currency") private String currency;
    @JsonProperty("ota_booking_id") private String ota_booking_id;
    @JsonProperty("ota_revision_id") private String ota_revision_id;
    @JsonProperty("pay_hote_ota") private String pay_hote_ota;
    @JsonProperty("Nationality") private String Nationality;
    @JsonProperty("meal_plan_optio") private String meal_plan_optio;
}
