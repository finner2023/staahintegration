package com.finner.integration.staah_integration.Service;

import com.finner.integration.staah_integration.Model.ProcessedReservationCancelDTO;
import com.finner.integration.staah_integration.Model.ProcessedReservationModifiedDTO;
import com.finner.integration.staah_integration.Model.ProcessedReservationNewDTO;
import com.finner.integration.staah_integration.Model.Room;
import com.finner.integration.staah_integration.Model.StaahReservation;
import com.finner.integration.staah_integration.util.ReservationMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationProcessor {

    public ProcessedReservationNewDTO processNew(StaahReservation reservation, Room room) {
        return ReservationMapper.mapToNewProcessed(reservation, room);
    }

    public ProcessedReservationModifiedDTO processModified(StaahReservation reservation, Room room, String modificationReason) {
        return ReservationMapper.mapToModifiedProcessed(reservation, room, modificationReason);
    }

    public ProcessedReservationCancelDTO processCancel(StaahReservation reservation, Room room, String cancelReason) {
        return ReservationMapper.mapToCancelProcessed(reservation, room, cancelReason);
    }
}
