package com.finner.integration.staah_integration.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude nulls from payload
public class ProcessedReservationNewDTO {

    @JsonProperty("guest_name")
    private String guest_name;

    @JsonProperty("guest_email")
    private String guest_email;

    @JsonProperty("guest_phone")
    private Long guest_phone; // ✅ Changed from String to Long (PMS expects number)

    @JsonProperty("checkIn_date")
    private String checkIn_date; // ✅ PMS expects date format - make sure it's "YYYY-MM-DD"

    @JsonProperty("checkOut_date")
    private String checkOut_date;

    @JsonProperty("room_category")
    private String room_category; // PMS type: a_prop_Room_category

    @JsonProperty("tax_amount")
    private Double tax_amount; // ✅ Changed to Double (PMS expects number)

    @JsonProperty("meal_plan")
    private String meal_plan;

    @JsonProperty("ota_property_id")
    private String ota_property_id;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("no_of_guest")
    private Integer no_of_guest; // ✅ Changed to Integer (PMS expects number)

    @JsonProperty("ota_room_id")
    private String ota_room_id;

    @JsonProperty("phone_countryCode")
    private String phone_countryCode;

    @JsonProperty("guest_address")
    private String guest_address;

    @JsonProperty("Nationality")
    private String Nationality; // ✅ Corrected typo from 'Natonality' to 'Nationality'

    @JsonProperty("ota_booking_revision_id")
    private String ota_booking_revision_id;

    @JsonProperty("total_amount")
    private Double total_amount; // ✅ Changed to Double (PMS expects number)

    @JsonProperty("pms_transaction_id")
    private String pms_transaction_id;

    @JsonProperty("pay_hotel_OTA")
    private String pay_hotel_OTA;

    @JsonProperty("OTA_Channel")
    private String OTA_Channel;

    @JsonProperty("ota_booking_id")
    private String ota_booking_id;

    @JsonProperty("meal_plan_optionset")
    private String meal_plan_optionset;

    @JsonProperty("channel_manager_id")
    private String channel_manager_id;
}
