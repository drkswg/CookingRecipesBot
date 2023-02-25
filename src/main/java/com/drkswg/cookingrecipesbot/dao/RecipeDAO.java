package com.drkswg.cookingrecipesbot.dao;

import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeCategory;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.entity.User;

import java.util.List;

public interface RecipeDAO {
    List<Recipe> getRecipes(String type);
    Recipe getRecipe(String name);
    RecipeStep getNextStep(Recipe recipe, int step);
    User addUserIfNotExist(long id, String userName);
    RecipeCategory getRecipeCategory(String categoryName);
    Recipe addNewRecipeAuthorAndCategory(User author, RecipeCategory recipeCategory);
    Recipe getBlankRecipe(User author);
    void updateRecipe(Recipe recipe);
    Recipe getRecipeWithNoDescription(User author);
    void deleteNotFinishedRecipes(long userId);
}
