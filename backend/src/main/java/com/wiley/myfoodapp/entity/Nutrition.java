package com.wiley.myfoodapp.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class Nutrition {

    @JsonAlias(value = "nutrients")
    List<Nutrient> recipeNutrients;
    List<Ingredient> ingredients;
}
