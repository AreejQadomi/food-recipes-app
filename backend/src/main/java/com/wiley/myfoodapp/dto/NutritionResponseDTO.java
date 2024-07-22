package com.wiley.myfoodapp.dto;

import com.wiley.myfoodapp.entity.Ingredient;
import com.wiley.myfoodapp.entity.Nutrient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NutritionResponseDTO {

    List<Nutrient> nutrients;
    List<Ingredient> ingredients;
}
