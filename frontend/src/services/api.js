import {
    getRandomRecipesUrl, getRecipeInformationUrl, getSearchRecipesUrl
} from "./api-properties";

export const fetchRecipes = async (query, page, pageSize) => {
    try {
        const response = await fetch(getSearchRecipesUrl(query, page, pageSize));
        return await handleResponse(response);
    } catch (error) {
        console.error(`API error happened while fetching recipes for queryString ${query}`, error);
        throw error;
    }
};

export const fetchRecipeById = async (id) => {
    try {
        const response = await fetch(getRecipeInformationUrl(id));
        return await handleResponse(response);
    } catch (error) {
        console.error(`API error happened while fetching recipe with id ${id}`, error);
        throw error;
    }
};

export const fetchRandomRecipes = async () => {
    try {
        const response = await fetch(getRandomRecipesUrl());
        return await handleResponse(response);
    } catch (error) {
        console.error('API error happened while fetching random recipes', error);
        throw error;
    }
};

const handleResponse = async (response) => {
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Something went wrong');
    }
    return response.json();
};