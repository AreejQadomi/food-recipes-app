package com.wiley.myfoodapp.service.impl;

import static com.wiley.myfoodapp.constants.ErrorMessages.CALORIES_CALCULATION_ERROR_MESSAGE_2;
import static com.wiley.myfoodapp.constants.ResponseMessages.CALORIES_CALCULATION_WARNING_MESSAGE_1;
import static com.wiley.myfoodapp.constants.ResponseMessages.CALORIES_CALCULATION_WARNING_MESSAGE_2;
import static com.wiley.myfoodapp.constants.ResponseMessages.CALORIES_CALCULATION_WARNING_MESSAGE_3;

import com.wiley.myfoodapp.entity.Ingredient;
import com.wiley.myfoodapp.entity.Nutrient;
import com.wiley.myfoodapp.entity.RecipeInformation;
import com.wiley.myfoodapp.mapper.RecipeInformationMapper;
import com.wiley.myfoodapp.dto.NutritionResponseDTO;
import com.wiley.myfoodapp.dto.CaloriesCalculationDTO;
import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;
import com.wiley.myfoodapp.service.RecipeService;
import com.wiley.myfoodapp.service.SpoonacularService;
import com.wiley.myfoodapp.dto.RecipeInformationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private SpoonacularService spoonacularService;

    private static final String CALORIES = "Calories";

    @Override
    public RecipeDTO findRecipes(String query, int pageSize, int page) {
        return spoonacularService.searchRecipes(query, pageSize, page);
    }

    @Override
    public RecipeInformationDTO findRecipeById(Long id) {
        RecipeInformation recipeInformation = spoonacularService.getRecipeInformation(id);

        RecipeInformationDTO
            recipeInformationDto =
            RecipeInformationMapper.convertToRecipeInformationDTO(recipeInformation);
        recipeInformationDto.setTotalCalories(calculateTotalCalories(recipeInformation));
        return recipeInformationDto;
    }

    @Override
    public CaloriesCalculationDTO getCaloriesByRecipeId(Long id, String excludedIngredients) {
//        if (!StringUtils.hasLength(excludedIngredients)) {
//            throw new IllegalArgumentException(CALORIES_CALCULATION_ERROR_MESSAGE_1);
//        }

        NutritionResponseDTO
            nutrition =
            spoonacularService.getNutritionInformation(id, excludedIngredients);

        String warningMessage = "";
        double totalCalories = getTotalCalories(id, nutrition);
        List<Ingredient> recipeIngredients = nutrition.getIngredients();
        List<String> excludedIngredientsList = List.of(excludedIngredients.split(",\\s*"));

        if (isAllIngredientsExcluded(excludedIngredientsList, recipeIngredients)) {
            warningMessage = CALORIES_CALCULATION_WARNING_MESSAGE_1;
            return CaloriesCalculationDTO.builder().totalCalories(0.0).warnings(warningMessage)
                .build();
        }

        List<String>
            missingIngredients =
            excludedIngredientsList.stream().filter(ingredient -> recipeIngredients.stream()
                .noneMatch(i -> i.getName().equalsIgnoreCase(ingredient))).toList();

        if (!missingIngredients.isEmpty()) {
            if (missingIngredients.size() == excludedIngredientsList.size()) {
                warningMessage = CALORIES_CALCULATION_WARNING_MESSAGE_2;
                return CaloriesCalculationDTO.builder().totalCalories(totalCalories)
                    .warnings(warningMessage).build();
            }

            warningMessage =
                String.format(CALORIES_CALCULATION_WARNING_MESSAGE_3, missingIngredients);
        }

        double
            excludedCalories =
            calculateExcludedCalories(recipeIngredients, excludedIngredientsList);

        return CaloriesCalculationDTO.builder().totalCalories(totalCalories - excludedCalories)
            .warnings(warningMessage).build();

    }

    @Override
    public RandomRecipesDTO getRandomRecipes() {
        return spoonacularService.getRandomRecipes();
    }

    private boolean isAllIngredientsExcluded(List<String> excludedIngredientsList,
                                             List<Ingredient> recipeIngredients) {
        return new HashSet<>(excludedIngredientsList).containsAll(
            recipeIngredients.stream().map(Ingredient::getName).toList());
    }

    private double calculateExcludedCalories(List<Ingredient> ingredients,
                                             List<String> excludedIngredientsList) {
        return ingredients.stream()
            .filter(ingredient -> excludedIngredientsList.contains(ingredient.getName()))
            .flatMap(ingredient -> ingredient.getNutrients().stream())
            .filter(nutrient -> CALORIES.equals(nutrient.getName()))
            .mapToDouble(Nutrient::getAmount).sum();
    }

    private double calculateTotalCalories(RecipeInformation recipeInformation) {

        List<Ingredient> ingredients = recipeInformation.getNutrition().getIngredients();

        double
            totalCalories =
            ingredients.stream().flatMap(ingredient -> ingredient.getNutrients().stream())
                .filter(nutrient -> CALORIES.equals(nutrient.getName()))
                .mapToDouble(Nutrient::getAmount).sum();

        return Math.round(totalCalories * 100.0) / 100.0;
    }

    private double getTotalCalories(Long id, NutritionResponseDTO nutrition) {
        Nutrient
            caloriesNutrient =
            nutrition.getNutrients().stream().filter(n -> CALORIES.equals(n.getName())).findFirst()
                .orElseThrow(() -> new RuntimeException(
                    String.format(CALORIES_CALCULATION_ERROR_MESSAGE_2, id)));
        return caloriesNutrient.getAmount();
    }
}
