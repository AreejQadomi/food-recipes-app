package com.wiley.myfoodapp.entity;

import lombok.Data;

import java.util.List;

@Data
public class RecipeInformation {

    private Long id;
    private String title;
    private String image;
    private int servings;
    private int readyInMinutes;
    private String summary;
    private List<Ingredient> extendedIngredients;
    private String instructions;
    private Nutrition nutrition;
    private String sourceUrl;
}
