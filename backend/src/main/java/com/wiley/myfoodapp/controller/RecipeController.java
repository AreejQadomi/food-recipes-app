package com.wiley.myfoodapp.controller;

import com.wiley.myfoodapp.dto.CaloriesCalculationDTO;
import com.wiley.myfoodapp.dto.RandomRecipesDTO;
import com.wiley.myfoodapp.dto.RecipeDTO;
import com.wiley.myfoodapp.service.RecipeService;
import com.wiley.myfoodapp.dto.RecipeInformationDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/search")
    public ResponseEntity<RecipeDTO> getRecipes(
        @RequestParam @NotBlank @Size(min = 3, max = 50) String query,
        @RequestParam(defaultValue = "${spoonacular.recipes.results-per-page}") @Min(5) int pageSize,
        @RequestParam(defaultValue = "${spoonacular.recipes.results-start-page}") @Min(1) int page) {
        return new ResponseEntity<>(recipeService.findRecipes(query, pageSize, page),
                                    HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeInformationDTO> getRecipeInformationById(
        @PathVariable @NotNull Long id) {
        return new ResponseEntity<>(recipeService.findRecipeById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/calories")
    @Description("Get recipe calories providing the recipe id and a list of excluded ingredients")
    public ResponseEntity<CaloriesCalculationDTO> getRecipeCaloriesById(
        @PathVariable @NotNull Long id, @RequestParam @NotBlank String excludedIngredients) {
        return new ResponseEntity<>(recipeService.getCaloriesByRecipeId(id, excludedIngredients),
                                    HttpStatus.OK);
    }

    @GetMapping("/random")
    public ResponseEntity<RandomRecipesDTO> getRandomRecipe() {
        return new ResponseEntity<>(recipeService.getRandomRecipes(), HttpStatus.OK);
    }

}
