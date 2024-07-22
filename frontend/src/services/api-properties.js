const BASE_URL = "http://localhost:8080/api/recipes";
const SEARCH_RECIPES_URL = `${BASE_URL}/search`;
const RANDOM_RECIPES_URL = `${BASE_URL}/random`;
const DEFAULT_PAGE_SIZE = 10;
const DEFAULT_PAGE = 1;

export const getSearchRecipesUrl = (query, page = DEFAULT_PAGE, pageSize = DEFAULT_PAGE_SIZE) => `${SEARCH_RECIPES_URL}?query=${query}&page=${page}&pageSize=${pageSize}`;
export const getRandomRecipesUrl = () => RANDOM_RECIPES_URL;
export const getRecipeInformationUrl = (recipeId) => `${BASE_URL}/${recipeId}`;
export const getRecipeInformationPath = (recipeId) => `/recipes/${recipeId}`;
export {DEFAULT_PAGE_SIZE, DEFAULT_PAGE};