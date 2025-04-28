package com.finner.integration.staah_integration.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor        // Generates default no-args constructor
@AllArgsConstructor
public class Customer {
    private String first_name;
    private String last_name;
    private String address;
    private String email;
    private String telephone;
    private String countrycode;


}
