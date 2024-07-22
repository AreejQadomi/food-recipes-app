package com.wiley.myfoodapp.mapper;

import com.wiley.myfoodapp.entity.RecipeInformation;
import com.wiley.myfoodapp.dto.RecipeInformationDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RecipeInformationMapper {

    public static RecipeInformationDTO convertToRecipeInformationDTO(
        RecipeInformation recipeInformation) {

        return RecipeInformationDTO.builder()
            .id(recipeInformation.getId())
            .title(recipeInformation.getTitle())
            .image(recipeInformation.getImage())
            .servings(recipeInformation.getServings())
            .preparationTime(recipeInformation.getReadyInMinutes())
            .instructions(recipeInformation.getInstructions())
            .ingredients(recipeInformation.getExtendedIngredients())
            .summary(recipeInformation.getSummary())
            .sourceUrl(recipeInformation.getSourceUrl())
            .build();
    }
}
