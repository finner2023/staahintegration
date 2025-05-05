package com.finner.integration.staah_integration.util;

import com.finner.integration.staah_integration.Model.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.finner.integration.staah_integration.util.OtaChannelMapper.OTA_CHANNEL_MAP;

@Slf4j
public class ReservationMapper {



    public static ProcessedReservationNewDTO mapToNewProcessed(StaahReservation reservation, Room room) {
        Customer customer = reservation.getCustomer();
        int days = calculateDays(room.getArrival_date(), room.getDeparture_date());
        Price price = room.getPrice() != null && !room.getPrice().isEmpty() ? room.getPrice().get(0) : new Price();
        Affiliation affiliation=reservation.getAffiliation();
        String rawSource = affiliation.getSource(); // or getPos()
        String normalized = rawSource != null ? rawSource.trim().toLowerCase() : "";
        String otaChannel = OTA_CHANNEL_MAP.getOrDefault(normalized, "Unknown");
        return ProcessedReservationNewDTO.builder()
                .guest_name(customer.getFirst_name() + " " + customer.getLast_name())
                .guest_email(customer.getEmail())
                .guest_phone(safeParseLong(customer.getTelephone()))
                .checkIn_date(room.getArrival_date())
                .checkOut_date(room.getDeparture_date())
//                .room_category(room.getId()) // Use ID as PMS expects option set ID
                .tax_amount(parseDouble(room.getTotaltax()))
                .meal_plan(price.getRate_id())
                .ota_property_id(reservation.getHotel_id())
                .currency(reservation.getCurrencycode())
                .no_of_guest(parseInt(room.getNumberofguests()))
                .ota_room_id(room.getId())//not imp
                .phone_countryCode(customer.getCountrycode())
                .guest_address(customer.getAddress())
                .Nationality(customer.getCountrycode())
                .ota_booking_revision_id(reservation.getReservation_notif_id())
                .total_amount(parseDouble(reservation.getCommissionamount()))
                .pms_transaction_id(room.getRoomreservation_id())
                .pay_hotel_OTA(reservation.getPaymenttype())
                .OTA_Channel(otaChannel)// Mapped from affiliation/source field
                //prepare optionset for this
               // .OTA_Channel("BookingCom")//map it with pms optionset by writing enum
                .ota_booking_id(reservation.getChannel_booking_id()) // Actual OTA booking ID
               // .meal_plan_optionset(price.getMealplan_id())
                .channel_manager_id(reservation.getHotel_id())
                .build();
    }

    public static ProcessedReservationModifiedDTO mapToModifiedProcessed(StaahReservation reservation, Room room, String modificationReason) {
        Customer customer = reservation.getCustomer();
        int days = calculateDays(room.getArrival_date(), room.getDeparture_date());
        Price price = room.getPrice() != null && !room.getPrice().isEmpty() ? room.getPrice().get(0) : new Price();

        Affiliation affiliation=reservation.getAffiliation();
        String rawSource = affiliation.getSource(); // or getPos()
        String normalized = rawSource != null ? rawSource.trim().toLowerCase() : "";
        String otaChannel = OTA_CHANNEL_MAP.getOrDefault(normalized, "Unknown");
        return ProcessedReservationModifiedDTO.builder()
                .guest_name(customer.getFirst_name() + " " + customer.getLast_name())
                .guest_email(customer.getEmail())
                .totalamount(reservation.getCommissionamount()) // Adjust if needed
                .tax_amount(parseDouble(room.getTotaltax()))
                .OTA_Channel(otaChannel) // Adjust accordingly
                .guestphone(String.valueOf(customer.getTelephone()))
                .checkin_date(room.getArrival_date())
                .checkOut_date(room.getDeparture_date())
                .phone_countryCode(customer.getCountrycode())
                .room_category(room.getRoomType())
                .no_of_guest(parseInt(room.getNumberofguests()))
                .currency(reservation.getCurrencycode())
                .meal_plan(price.getRate_id())//************
                .ota_booking_id(reservation.getChannel_booking_id())  // Adjust accordingly
                .ota_property_id(reservation.getHotel_id())
                .ota_room_id(room.getId())
                .ota_booking_rev(reservation.getReservation_notif_id()) // Adjust accordingly
                .pay_hotel_OTA(reservation.getPaymenttype())
                .Natonality(customer.getCountrycode())
                .guest_address(customer.getAddress())
              //  .meal_plan_optionset(price.getMealplan_id())
                .channel_manager_id(reservation.getHotel_id())
                // For modifications, include any modification reasoning, if required. Since field names match your screenshot exactly,
                // we assume the modification reason is passed in a separate mechanism. For now, if not used, it can be left empty.
                .modification_reason(modificationReason)
                .build();
    }

    public static ProcessedReservationCancelDTO mapToCancelProcessed(StaahReservation reservation, Room room, String cancelReason) {
        Customer customer = reservation.getCustomer();
        int days = calculateDays(room.getArrival_date(), room.getDeparture_date());
        Price price = room.getPrice() != null && !room.getPrice().isEmpty() ? room.getPrice().get(0) : new Price();

        Affiliation affiliation=reservation.getAffiliation();
        String rawSource = affiliation.getSource(); // or getPos()
        String normalized = rawSource != null ? rawSource.trim().toLowerCase() : "";
        String otaChannel = OTA_CHANNEL_MAP.getOrDefault(normalized, "Unknown");
        return ProcessedReservationCancelDTO.builder()
                .parent_property("")
                .total_amount(Double.parseDouble(reservation.getTotalprice())) // carefully mapped
                .tax_amount(Double.parseDouble(reservation.getTotaltax()))
                .OTA_Channel(otaChannel) // fallback if affiliation missing
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
    public static GroupReservationDTO mapToGroupCommonDTO(StaahReservation reservation) {
        Customer customer = reservation.getCustomer();
        Room firstRoom = reservation.getRooms().get(0); // Assume first room for general dates, guest count, etc.
        Affiliation affiliation=reservation.getAffiliation();
        String rawSource = affiliation.getSource(); // or getPos()
        String normalized = rawSource != null ? rawSource.trim().toLowerCase() : "";
        String otaChannel = OTA_CHANNEL_MAP.getOrDefault(normalized, "Unknown");
        return GroupReservationDTO.builder()
                .guest_name(customer.getFirst_name() + " " + customer.getLast_name())
                .guest_email(customer.getEmail())
                .parent_property(reservation.getHotel_id())
                .total_amount(parseDouble(reservation.getTotalprice()))
                .tax_amount(parseDouble(reservation.getTotaltax()))
                .OTA_Channel(reservation.getAffiliation() != null ? reservation.getAffiliation().getPos() : "")
                .guest_phone(safeParseLong(customer.getTelephone()))
                .checkIn_date(firstRoom.getArrival_date())
                .checkOut_date(firstRoom.getDeparture_date())
                .phone_countryC(customer.getCountrycode())
                .no_of_guest(parseInt(firstRoom.getNumberofguests()))
                .guest_address(customer.getAddress())
                .currency(reservation.getCurrencycode())
                .ota_booking_id(reservation.getChannel_booking_id())
                .ota_revision_id(reservation.getReservation_notif_id())
                .pay_hote_ota(reservation.getPaymenttype())
                .Nationality(customer.getCountrycode())
                .meal_plan_optio("") // Optional as per UI screenshot
                .build();
    }


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
/*
* ota_option_Set
*
* */