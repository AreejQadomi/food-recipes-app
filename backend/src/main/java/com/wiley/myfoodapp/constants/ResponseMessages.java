package com.wiley.myfoodapp.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ResponseMessages {

    public static final String
        CALORIES_CALCULATION_WARNING_MESSAGE_1 =
        "All of the ingredients in the recipe are excluded, the total calorie count is 0.0.";
    public static final String
        CALORIES_CALCULATION_WARNING_MESSAGE_2 =
        "None of the ingredients are in the recipe, the total calorie count will be the same.";
    public static final String
        CALORIES_CALCULATION_WARNING_MESSAGE_3 =
        "The following ingredient(s) are not in the recipe: %s, however the total calorie count will be calculated among the existing ingredients.";
}
