package com.wiley.myfoodapp.service;

import com.wiley.myfoodapp.entity.RecipeInformation;
import com.wiley.myfoodapp.dto.NutritionResponseDTO;
import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;

/**
 * This interface defines the contract for interacting with the external Spoonacular API.
 */
public interface SpoonacularService {

    RecipeDTO searchRecipes(String query, int pageSize, int page);

    RecipeInformation getRecipeInformation(Long id);

    NutritionResponseDTO getNutritionInformation(Long id,
                                      String excludedIngredients);

    RandomRecipesDTO getRandomRecipes();
}
