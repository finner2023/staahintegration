package com.finner.integration.staah_integration.util;

import com.finner.integration.staah_integration.Model.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
@Slf4j
public class ReservationMapper {

    public static ProcessedReservationNewDTO mapToNewProcessed(StaahReservation reservation, Room room) {
        Customer customer = reservation.getCustomer();
        int days = calculateDays(room.getArrival_date(), room.getDeparture_date());
        Price price=reservation.getPrice().get(0);
        return ProcessedReservationNewDTO.builder()
                .guest_name(customer.getFirst_name() + " " + customer.getLast_name())
                .guest_email(customer.getEmail())
                .guest_phone(safeParseLong(customer.getTelephone()))
                .checkIn_date(room.getArrival_date())
                .checkOut_date(room.getDeparture_date())
                .room_category(room.getId()) // Use ID as PMS expects option set ID
                .tax_amount(parseDouble(room.getTotaltax()))
                .meal_plan(room.getPrice().get(0).getMealplan())
                .ota_property_id(reservation.getHotel_id())
                .currency(reservation.getCurrencycode())
                .no_of_guest(parseInt(room.getNumberofguests()))
                .ota_room_id(room.getRoomreservation_id())
                .phone_countryCode(customer.getCountrycode())
                .guest_address(customer.getAddress())
                .Nationality(customer.getCountrycode())
                .ota_booking_revision_id(reservation.getReservation_notif_id())
                .total_amount(parseDouble(reservation.getCommissionamount()))
                .pms_transaction_id(room.getRoomreservation_id())
                .pay_hotel_OTA(reservation.getPaymenttype())
                .OTA_Channel(reservation.getSource()) // Mapped from affiliation/source field
                .ota_booking_id(reservation.getChannel_booking_id()) // Actual OTA booking ID
                .meal_plan_optionset(price.getMealplan_id())
                .channel_manager_id(reservation.getHotel_id())
                .build();
    }

    public static ProcessedReservationModifiedDTO mapToModifiedProcessed(StaahReservation reservation, Room room, String modificationReason) {
        Customer customer = reservation.getCustomer();
        int days = calculateDays(room.getArrival_date(), room.getDeparture_date());
        Price price=reservation.getPrice().get(0);
        Affiliation affiliation=reservation.getAffiliation();

        return ProcessedReservationModifiedDTO.builder()
                .guest_name(customer.getFirst_name() + " " + customer.getLast_name())
                .guest_email(customer.getEmail())

                .totalamount(reservation.getCommissionamount()) // Adjust if needed
                .tax_amount(parseDouble(room.getTotaltax()))
                .OTAChannel(affiliation.getPos()) // Adjust accordingly
                .guestphone(String.valueOf(customer.getTelephone()))
                .checkin_date(room.getArrival_date())
                .checkOutdate(room.getDeparture_date())
                .phone_countryCode(customer.getCountrycode())
                .room_category(room.getRoomType())
                .no_of_guest(parseInt(room.getNumberofguests()))
                .currency(reservation.getCurrencycode())
                .meal_plan(price.getMealplan())
                .otabooking_id("OTABOOKINGID")  // Adjust accordingly
                .ota_property_id(reservation.getHotel_id())
                .otaroomid(room.getRoomreservation_id())
                .ota_booking_rev(reservation.getReservation_notif_id()) // Adjust accordingly
                .pay_hotelOTA(reservation.getPaymenttype())
                .Natonality(customer.getCountrycode())
                .guest_address(customer.getAddress())
                .meal_plan_optionset(price.getMealplan_id())
                .channel_manager_id(reservation.getHotel_id())
                // For modifications, include any modification reasoning, if required. Since field names match your screenshot exactly,
                // we assume the modification reason is passed in a separate mechanism. For now, if not used, it can be left empty.
                .modification_reason(modificationReason)
                .build();
    }

    public static ProcessedReservationCancelDTO mapToCancelProcessed(StaahReservation reservation, Room room, String cancelReason) {
        Customer customer = reservation.getCustomer();
        int days = calculateDays(room.getArrival_date(), room.getDeparture_date());
        Price price=reservation.getPrice().get(0);
        Affiliation affiliation=reservation.getAffiliation();
        return ProcessedReservationCancelDTO.builder()
                .parent_property("")
                .total_amount(Double.parseDouble(reservation.getTotalprice())) // carefully mapped
                .tax_amount(Double.parseDouble(reservation.getTotaltax()))
                .OTA_Channel(affiliation != null ? affiliation.getPos() : "Unknown") // fallback if affiliation missing
                .checkIn_date(room.getArrival_date())
                .checkOut_date(room.getDeparture_date())
                .room_category(room.getId()) // or room.getRoomType() if needed
                .ota_booking_id(reservation.getChannel_booking_id())
                .ota_property_id(reservation.getHotel_id())
                .ota_room_id(room.getRoomreservation_id())
                .ota_booking_rev(reservation.getReservation_notif_id())
                .build();
    }

    // Helper methods


    private static int calculateDays(String checkin, String checkout) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inDate = LocalDate.parse(checkin, formatter);
            LocalDate outDate = LocalDate.parse(checkout, formatter);
            return (int) ChronoUnit.DAYS.between(inDate, outDate);
        } catch (Exception e) {
            log.warn("⚠️ Failed to calculate days between [{}] and [{}]: {}", checkin, checkout, e.getMessage());
            return 1;
        }
    }

    private static double parseDouble(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static int parseInt(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }
    private static Long safeParseLong(String value) {
        try {
            return Long.parseLong(value.replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return null;
        }
    }


}
