package com.wiley.myfoodapp.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.wiley.myfoodapp.config.SpoonacularProperties;
import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.entity.Ingredient;
import com.wiley.myfoodapp.entity.Nutrient;
import com.wiley.myfoodapp.entity.Recipe;
import com.wiley.myfoodapp.entity.RecipeInformation;
import com.wiley.myfoodapp.dto.NutritionResponseDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;
import com.wiley.myfoodapp.service.impl.SpoonacularServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
@ExtendWith(MockitoExtension.class)
public class SpoonacularServiceImplUnitTest {

    private static final String QUERY = "pasta";
    private static final int PAGE_SIZE = 10;
    private static final int PAGE = 1;

    @InjectMocks
    private SpoonacularServiceImpl spoonacularService;

    @Mock
    private SpoonacularProperties properties;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        // Init the service mock
        spoonacularService = new SpoonacularServiceImpl(properties, webClient);

        // Set up the WebClient mock
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void testFindRecipes() {
        // Given - the mock response
        RecipeDTO mockResponse = new RecipeDTO();
        Recipe recipe = new Recipe();
        recipe.setId(1234L);
        recipe.setTitle("Pasta and Cheese");
        recipe.setImage("https://img.spoonacular.com/recipes/642583-312x231.jpg");

        mockResponse.setResults(singletonList(recipe));

        when(responseSpec.bodyToMono(RecipeDTO.class)).thenReturn(Mono.just(mockResponse));

        // When - the service function is invoked
        RecipeDTO recipeDTO = spoonacularService.searchRecipes(QUERY, PAGE_SIZE, PAGE);

        // Then - the expected response should be returned
        assertThat(recipeDTO).isNotNull();
        assertThat(recipeDTO.getResults()).hasSize(1);
        assertThat(recipeDTO.getResults().get(0)).isEqualTo(recipe);
    }

    @Test
    void testFindRecipesWithEmptyResponse() {
        // Given - the mock response
        when(responseSpec.bodyToMono(RecipeDTO.class)).thenReturn(Mono.empty());

        // When - the service function is invoked
        RecipeDTO recipeDTO = spoonacularService.searchRecipes(QUERY, PAGE_SIZE, PAGE);

        // Then - the expected response should be empty
        assertThat(recipeDTO).isNull();
    }

    @Test
    void testFindRecipeById() {
        // Given - the mock response
        Long id = 1234L;
        RecipeInformation mockResponse = new RecipeInformation();
        mockResponse.setId(id);
        mockResponse.setTitle("Pasta and Cheese");
        mockResponse.setImage("https://img.spoonacular.com/recipes/642583-312x231.jpg");

        when(responseSpec.bodyToMono(RecipeInformation.class)).thenReturn(Mono.just(mockResponse));

        // When - the service function is invoked
        RecipeInformation recipe = spoonacularService.getRecipeInformation(id);

        // Then - the expected response should be returned
        assertThat(recipe).isEqualTo(mockResponse);
    }

    @Test
    void testGetCaloriesByRecipeId() {
        // Given - the mock response
        Long id = 1234L;
        String excludedIngredients = "pasta,cheese";
        NutritionResponseDTO mockResponse = getMockResponse();
        when(responseSpec.bodyToMono(NutritionResponseDTO.class)).thenReturn(
            Mono.just(mockResponse));

        // When - the service function is invoked
        NutritionResponseDTO
            response =
            spoonacularService.getNutritionInformation(id, excludedIngredients);

        // Then - the expected response should be returned
        assertThat(response).isEqualTo(mockResponse);
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

        when(responseSpec.bodyToMono(RandomRecipesDTO.class)).thenReturn(Mono.just(mockResponse));
        when(properties.getRecipes()).thenReturn(mock(SpoonacularProperties.RecipesProperties.class));
        when(properties.getRecipes().getRandomRecipesCount()).thenReturn(4);

        // When - the service function is invoked
        RandomRecipesDTO randomRecipesDTO = spoonacularService.getRandomRecipes();

        // Then - the expected response should be returned
        assertThat(randomRecipesDTO).isEqualTo(mockResponse);
    }

    private NutritionResponseDTO getMockResponse() {

        double totalCalories = 170.0;
        double pastaCalories = 70.0;
        double cheeseCalories = 100.0;

        Ingredient pasta = getIngredient("pasta", pastaCalories, 10.0);
        Ingredient cheese = getIngredient("cheese", cheeseCalories, 20.0);

        return NutritionResponseDTO.builder().ingredients(Arrays.asList(pasta, cheese)).nutrients(
            Arrays.asList(Nutrient.builder().name("Calories").amount(totalCalories).build(),
                          Nutrient.builder().name("Protein").amount(30.0).build())).build();
    }

    public static Ingredient getIngredient(String name, double calories, double protein) {
        return Ingredient.builder().name(name).nutrients(
            Arrays.asList(Nutrient.builder().name("Calories").amount(calories).build(),
                          Nutrient.builder().name("Protein").amount(protein).build())).build();
    }
}
