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
public class ProcessedReservationModifiedDTO {

    @JsonProperty("guest_name")
    private String guest_name;

    @JsonProperty("guest_email")
    private String guest_email;

    @JsonProperty("parent_property")
    private String parent_property;

    @JsonProperty("totalamount")
    private String totalamount;

    @JsonProperty("tax_amount")
    private double tax_amount;

    @JsonProperty("OTA_Channel")
    private String OTA_Channel;

    @JsonProperty("guestphone")
    private String guestphone;

    @JsonProperty("checkin_date")
    private String checkin_date;

    @JsonProperty("checkOut_date")
    private String checkOut_date;

    @JsonProperty("phone_countryCode")
    private String phone_countryCode;

    @JsonProperty("room_category")
    private String room_category;

    @JsonProperty("no_of_guest")
    private int no_of_guest;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("meal_plan")
    private String meal_plan;

    @JsonProperty("ota_booking_id")
    private String ota_booking_id;

    @JsonProperty("ota_property_id")
    private String ota_property_id;

    @JsonProperty("ota_room_id")
    private String ota_room_id;

    @JsonProperty("ota_booking_rev")
    private String ota_booking_rev;

    @JsonProperty("pay_hotel_OTA")
    private String pay_hotel_OTA;

    @JsonProperty("Natonality")
    private String Natonality;

    @JsonProperty("guest_address")
    private String guest_address;

    @JsonProperty("meal_plan_optionset")
    private String meal_plan_optionset;

    @JsonProperty("modification_reason")
    private String modification_reason; // Extra field for modified reservations

    @JsonProperty("channel_manager_id")
    private String channel_manager_id;
    // Private constructor using Builder
    @JsonProperty("group_reservation_id")
    private String group_reservation_id;

}
