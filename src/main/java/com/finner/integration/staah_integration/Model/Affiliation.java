package com.finner.integration.staah_integration.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor        // Generates default no-args constructor
@AllArgsConstructor
public class Affiliation {
    private String pos;               // Booking source (e.g., Expedia)
    private String source;            // Could be same as pos or used differently
    private String OTA_Code;          // OTA identifier
    private String gstno;
    private String companyname;
    private String companyaddress;
}
