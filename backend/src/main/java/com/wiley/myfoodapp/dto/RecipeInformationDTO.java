package com.wiley.myfoodapp.dto;

import com.wiley.myfoodapp.entity.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeInformationDTO {

    private Long id;
    private String title;
    private String image;
    private int servings;
    private int preparationTime;
    private String instructions;
    private List<Ingredient> ingredients;
    private String summary;
    private String sourceUrl;
    private double totalCalories;
}
