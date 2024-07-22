package com.wiley.myfoodapp.service;

import com.wiley.myfoodapp.dto.CaloriesCalculationDTO;
import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;
import com.wiley.myfoodapp.dto.RecipeInformationDTO;

/**
 * This is the main service interface used for Recipe related operations.
 */
public interface RecipeService {

    RecipeDTO findRecipes(String query, int pageSize, int page);

    RecipeInformationDTO findRecipeById(Long id);

    CaloriesCalculationDTO getCaloriesByRecipeId(Long id, String excludedIngredients);

    RandomRecipesDTO getRandomRecipes();
}
