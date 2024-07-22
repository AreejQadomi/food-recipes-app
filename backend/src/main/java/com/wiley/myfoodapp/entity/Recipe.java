package com.wiley.myfoodapp.entity;

import lombok.Data;

@Data
public class Recipe {

    private Long id;
    private String title;
    private String image;
    private String readyInMinutes;
    private String servings;
    private String spoonacularSourceUrl;
}
