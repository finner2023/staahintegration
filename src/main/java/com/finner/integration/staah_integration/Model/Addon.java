package com.finner.integration.staah_integration.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Addon {
    private String name;
    private String nights;
    private String priceperunit;
    private String pricemode;
    private String price;
    private String comment;
    private String pricebeforetax;
    private String tax;
}
