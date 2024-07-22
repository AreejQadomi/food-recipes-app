package com.wiley.myfoodapp.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ErrorMessages {

    public static final String
        CALORIES_CALCULATION_ERROR_MESSAGE_1 =
        "Could not find recipe with id %d";
    public static final String
        CALORIES_CALCULATION_ERROR_MESSAGE_2 =
        "Calories information is missing for recipe id %d";
}
