package com.wiley.myfoodapp.service.impl;

import static com.wiley.myfoodapp.constants.ErrorMessages.CALORIES_CALCULATION_ERROR_MESSAGE_1;
import static java.util.Objects.nonNull;

import com.wiley.myfoodapp.config.SpoonacularProperties;
import com.wiley.myfoodapp.entity.RecipeInformation;
import com.wiley.myfoodapp.dto.NutritionResponseDTO;
import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;
import com.wiley.myfoodapp.service.SpoonacularService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
@Slf4j
public class SpoonacularServiceImpl implements SpoonacularService {

    private SpoonacularProperties properties;
    private WebClient webClient;

    private static final String QUERY_PARAM = "query";
    private static final String NUMBER_PARAM = "number";
    private static final String OFFSET_PARAM = "offset";
    private static final String ADD_RECIPE_INFORMATION = "addRecipeInformation";
    private static final String INCLUDE_NUTRITION_PARAM = "includeNutrition";
    private static final String EXCLUDED_INGREDIENTS_PARAM = "excludedIngredients";

    @Override
    public RecipeDTO searchRecipes(String query, int pageSize, int page) {
        RecipeDTO
            recipeDTO =
            webClient.get().uri(
                    uriBuilder -> uriBuilder.path(properties.getRecipes().getSearchPath())
                        .queryParam(QUERY_PARAM, query).queryParam(NUMBER_PARAM, pageSize)
                        .queryParam(ADD_RECIPE_INFORMATION, true)
                        .queryParam(OFFSET_PARAM, pageSize * (page - 1)).build()).retrieve()
                .bodyToMono(RecipeDTO.class)
                .doOnError(error -> log.error("Error retrieving recipes: {}", error.getMessage()))
                .block();

        if (nonNull(recipeDTO)) {
            log.info("A total of ({}) Recipes have been fetched, query: {}.",
                     recipeDTO.getResults().size(), query);
        }
        return recipeDTO;
    }

    @Override
    public RecipeInformation getRecipeInformation(Long id) {
        RecipeInformation
            recipeInformation =
            webClient.get().uri(
                    uriBuilder -> uriBuilder.path(properties.getRecipes().getInformationPath())
                        .queryParam(INCLUDE_NUTRITION_PARAM, true).build(id)).retrieve()
                .bodyToMono(RecipeInformation.class)
                .doOnError(error -> log.error("Error retrieving recipe by id: {}", id, error))
                .block();

        if (nonNull(recipeInformation)) {
            log.info("Recipe information has been fetched, recipe id: {}.", id);
        }
        return recipeInformation;
    }

    @Override
    public NutritionResponseDTO getNutritionInformation(Long id, String excludedIngredients) {
        NutritionResponseDTO
            nutritionDTO =
            webClient.get().uri(
                    uriBuilder -> uriBuilder.path(properties.getRecipes().getNutritionPath())
                        .queryParam(EXCLUDED_INGREDIENTS_PARAM, excludedIngredients).build(id))
                .retrieve().bodyToMono(NutritionResponseDTO.class).doOnError(
                    error -> log.error("Error retrieving nutrition info for recipe id: {}", id, error))
                .blockOptional().orElseThrow(() -> new IllegalArgumentException(
                    String.format(CALORIES_CALCULATION_ERROR_MESSAGE_1, id)));

        if (nonNull(nutritionDTO)) {
            log.info("Nutrition information has been fetched, recipe id: {}.", id);
        }

        return nutritionDTO;
    }

    @Override
    public RandomRecipesDTO getRandomRecipes() {
        int randomRecipesCount = properties.getRecipes().getRandomRecipesCount();

        RandomRecipesDTO
            randomRecipesDTO =
            webClient.get().uri(
                    uriBuilder -> uriBuilder.path(properties.getRecipes().getRandomRecipesPath())
                        .queryParam(NUMBER_PARAM, randomRecipesCount).build()).retrieve()
                .bodyToMono(RandomRecipesDTO.class)
                .doOnError(error -> log.error("Error retrieving random recipes.", error)).block();

        if (nonNull(randomRecipesDTO)) {
            log.info("{} Random recipes have been fetched.", randomRecipesCount);
        }

        return randomRecipesDTO;
    }
}
