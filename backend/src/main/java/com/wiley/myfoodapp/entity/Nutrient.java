package com.wiley.myfoodapp.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Nutrient {

    private String name;
    private double amount;
}
