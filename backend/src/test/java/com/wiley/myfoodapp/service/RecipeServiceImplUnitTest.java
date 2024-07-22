package com.wiley.myfoodapp.service;

import static com.wiley.myfoodapp.constants.ResponseMessages.CALORIES_CALCULATION_WARNING_MESSAGE_1;
import static com.wiley.myfoodapp.constants.ResponseMessages.CALORIES_CALCULATION_WARNING_MESSAGE_2;
import static com.wiley.myfoodapp.constants.ResponseMessages.CALORIES_CALCULATION_WARNING_MESSAGE_3;
import static com.wiley.myfoodapp.service.SpoonacularServiceImplUnitTest.getIngredient;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;
import com.wiley.myfoodapp.entity.Ingredient;
import com.wiley.myfoodapp.entity.Nutrient;
import com.wiley.myfoodapp.entity.Nutrition;
import com.wiley.myfoodapp.entity.Recipe;
import com.wiley.myfoodapp.entity.RecipeInformation;
import com.wiley.myfoodapp.dto.NutritionResponseDTO;
import com.wiley.myfoodapp.dto.CaloriesCalculationDTO;
import com.wiley.myfoodapp.service.impl.RecipeServiceImpl;
import com.wiley.myfoodapp.dto.RecipeInformationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplUnitTest {

    private static final String QUERY = "pasta";
    private static final int PAGE_SIZE = 10;
    private static final int PAGE = 1;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private SpoonacularService spoonacularService;

    @BeforeEach
    void setUp() {
        // Init the service mock
        recipeService = new RecipeServiceImpl(spoonacularService);
    }

    @Test
    void testFindRecipes() {
        // Given - the mock response
        Recipe recipe = new Recipe();
        recipe.setId(1234L);
        recipe.setTitle("Pasta and Cheese");
        recipe.setImage("https://img.spoonacular.com/recipes/642583-312x231.jpg");

        RecipeDTO mockResponse = new RecipeDTO();
        mockResponse.setResults(singletonList(recipe));
        mockResponse.setTotalResults(1);
        when(spoonacularService.searchRecipes(QUERY, PAGE_SIZE, PAGE)).thenReturn(mockResponse);

        // When - the service function is invoked
        RecipeDTO recipeDTO = recipeService.findRecipes(QUERY, PAGE_SIZE, PAGE);

        // Then - the expected response should be returned
        assertThat(recipeDTO).isEqualTo(mockResponse);
    }

    @Test
    void testFindRecipesWithEmptyResponse() {
        // Given - the mock response
        RecipeDTO mockResponse = new RecipeDTO();
        mockResponse.setResults(emptyList());
        mockResponse.setTotalResults(0);

        when(spoonacularService.searchRecipes(QUERY, PAGE_SIZE, PAGE)).thenReturn(mockResponse);

        // When - the service function is invoked
        RecipeDTO recipeDTO = recipeService.findRecipes(QUERY, PAGE_SIZE, PAGE);

        // Then - the expected response should be empty
        assertThat(recipeDTO.getResults()).isEmpty();
        assertThat(recipeDTO.getTotalResults()).isZero();
    }

    @Test
    void testFindRecipeById() {
        // Given - the mock response
        Long id = 1234L;
        RecipeInformation mockResponse = new RecipeInformation();
        mockResponse.setId(id);
        mockResponse.setTitle("Pasta and Cheese");
        mockResponse.setImage("https://img.spoonacular.com/recipes/642583-312x231.jpg");

        Nutrition nutrition = new Nutrition();
        Ingredient pasta = getIngredient("pasta", 100.0, 10.0);
        Ingredient cheese = getIngredient("cheese", 150.0, 20.0);
        nutrition.setIngredients(Arrays.asList(pasta,cheese));
        mockResponse.setNutrition(nutrition);

        when(spoonacularService.getRecipeInformation(id)).thenReturn(mockResponse);

        // When - the service function is invoked
        RecipeInformationDTO recipe = recipeService.findRecipeById(id);

        // Then - the expected response should be returned
        RecipeInformationDTO recipeInformationDTO = RecipeInformationDTO.builder()
            .id(id)
            .title("Pasta and Cheese")
            .image("https://img.spoonacular.com/recipes/642583-312x231.jpg")
            .totalCalories(250.0)
            .build();
        assertThat(recipe).isEqualTo(recipeInformationDTO);
    }

    @ParameterizedTest
    @MethodSource("testGetCaloriesByRecipeIdDataProvider")
    void testGetCaloriesByRecipeId(String excludedIngredients,
                                   NutritionResponseDTO mockResponse,
                                   double expectedCalories, String warnings) {
        // Given - the mock response
        Long id = 1234L;
        when(spoonacularService.getNutritionInformation(id, excludedIngredients)).thenReturn(
            mockResponse);

        // When - the service function is invoked
        CaloriesCalculationDTO
            response =
            recipeService.getCaloriesByRecipeId(id, excludedIngredients);

        // Then - the expected response should be returned
        CaloriesCalculationDTO
            expectedResponse =
            CaloriesCalculationDTO.builder().totalCalories(expectedCalories)
                .warnings(warnings)
                .build();

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void testGetRandomRecipes() {
        // Given - the mock response
        Long id = 1234L;
        RecipeInformation recipeInformation = new RecipeInformation();
        recipeInformation.setId(id);
        recipeInformation.setTitle("Pasta and Cheese");
        recipeInformation.setImage("https://img.spoonacular.com/recipes/642583-312x231.jpg");

        RandomRecipesDTO mockResponse = new RandomRecipesDTO();
        mockResponse.setResults(singletonList(recipeInformation));
        when(spoonacularService.getRandomRecipes()).thenReturn(mockResponse);

        // When - the service function is invoked
        RandomRecipesDTO randomRecipes = recipeService.getRandomRecipes();

        // Then - the expected response should be returned
        assertThat(randomRecipes).isEqualTo(mockResponse);
    }

    private static Stream<Arguments> testGetCaloriesByRecipeIdDataProvider() {

        double totalCalories = 170.0;
        double pastaCalories = 70.0;
        double cheeseCalories = 100.0;

        Ingredient pasta = getIngredient("pasta", pastaCalories, 10.0);
        Ingredient cheese = getIngredient("cheese", cheeseCalories, 20.0);

        NutritionResponseDTO
            mockResponse =
            NutritionResponseDTO.builder()
                .ingredients(Arrays.asList(pasta, cheese))
                .nutrients(
                    Arrays.asList(Nutrient.builder().name("Calories").amount(totalCalories).build(),
                                  Nutrient.builder().name("Protein").amount(30.0).build()))
                .build();

        String excludedIngredients1 = "cheese";
        String excludedIngredients2 = "pasta";
        String excludedIngredients3 = "pasta,cheese";
        String excludedIngredients4 = "cheese,carrot, cucumber";
        String excludedIngredients5 = "carrot, cucumber, lemon";

        String emptyWarning = "";
        String
            warningMsg =
            String.format(CALORIES_CALCULATION_WARNING_MESSAGE_3, "[carrot, cucumber]");

        // Data Provider Arguments:
        // 1. String excludedIngredients,
        // 2. SpoonacularNutritionResponseWrapper mockResponse,
        // 3. double expectedCalories,
        // 4. String warnings

        return Stream.of(
            Arguments.of(excludedIngredients1, mockResponse, pastaCalories, emptyWarning),
            Arguments.of(excludedIngredients2, mockResponse, cheeseCalories, emptyWarning),
            Arguments.of(excludedIngredients3, mockResponse, 0.0,
                         CALORIES_CALCULATION_WARNING_MESSAGE_1),
            Arguments.of(excludedIngredients4, mockResponse, pastaCalories, warningMsg),
            Arguments.of(excludedIngredients5, mockResponse, totalCalories,
                         CALORIES_CALCULATION_WARNING_MESSAGE_2));
    }
}
