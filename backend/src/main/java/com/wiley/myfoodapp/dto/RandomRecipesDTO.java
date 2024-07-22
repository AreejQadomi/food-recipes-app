package com.wiley.myfoodapp.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.wiley.myfoodapp.entity.RecipeInformation;
import lombok.Data;

import java.util.List;

@Data
public class RandomRecipesDTO {

    @JsonAlias(value = "recipes")
    private List<RecipeInformation> results;
}
