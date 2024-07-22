package com.wiley.myfoodapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "spoonacular")
@Data
public class SpoonacularProperties {

    @NestedConfigurationProperty
    private API api = new API();

    @NestedConfigurationProperty
    private RecipesProperties recipes = new RecipesProperties();

    @Data
    public static class API {

        private String key;
        private String baseUrl;
    }

    @Data
    public static class RecipesProperties {

        private String nutritionPath;
        private String informationPath;
        private String randomRecipesPath;
        private String searchPath;
        private int randomRecipesCount;
    }
}
