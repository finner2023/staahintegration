package com.finner.integration.staah_integration.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor        // Generates default no-args constructor
@AllArgsConstructor
public class Price {
    private String date;
    private String rate_id;
    private String mealplan_id;
    private String mealplan;
    private String tax;
    private String pricebeforetax;
    private String priceaftertax;

}
