package com.wiley.myfoodapp.dto;

import com.wiley.myfoodapp.entity.Recipe;
import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {

    private List<Recipe> results;
    private int totalResults;
}
