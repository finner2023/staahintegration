package com.finner.integration.staah_integration.Service;

import com.finner.integration.staah_integration.Model.*;
import com.finner.integration.staah_integration.util.ReservationMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationProcessor {

    public ProcessedReservationNewDTO processNew(StaahReservation reservation, Room room) {
        return ReservationMapper.mapToNewProcessed(reservation, room);
    }

    public static ProcessedReservationModifiedDTO processModified(StaahReservation reservation, Room room, String modificationReason) {
        return ReservationMapper.mapToModifiedProcessed(reservation, room, modificationReason);
    }
    public GroupReservationDTO processGroupReservation(StaahReservation reservation) {
        return ReservationMapper.mapToGroupCommonDTO(reservation);
    }

    public static ProcessedReservationCancelDTO processCancel(StaahReservation reservation, Room room, String cancelReason) {
        return ReservationMapper.mapToCancelProcessed(reservation, room, cancelReason);
    }
}
