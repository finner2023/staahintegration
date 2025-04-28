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
    @JsonProperty("otabooking_id")
    private String otabooking_id;

    @JsonProperty("ota_property_id")
    private String ota_property_id;

    @JsonProperty("reason_of_cancellation")
    private String reason_of_cancellation;

    @JsonProperty("pms_transaction_id")
    private String pms_transaction_id;

    @JsonProperty("cancelled_by")
    private String cancelled_by;

    @JsonProperty("guest_name")
    private String guest_name;

    @JsonProperty("guestphone")
    private String guestphone;

    @JsonProperty("guest_email")
    private String guest_email;

    @JsonProperty("OTAChannel")
    private String OTAChannel;

    @JsonProperty("parent_property")
    private String parent_property;


}
