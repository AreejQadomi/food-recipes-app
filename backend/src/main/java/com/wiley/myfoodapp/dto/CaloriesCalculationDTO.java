package com.wiley.myfoodapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaloriesCalculationDTO {

    private double totalCalories;
    private String warnings;
}
