package com.drkswg.cookingrecipesbot.service;

import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeCategory;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.entity.User;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes(String type);
    Recipe getRecipe(String name);
    RecipeStep getNextStep(Recipe recipe, int step);
    User addUserIfNotExist(long id, String userName);
    RecipeCategory getRecipeCategory(String categoryName);
    Recipe addNewRecipeAuthorAndCategory(User author, RecipeCategory recipeCategory);
    Recipe getBlankRecipe(User author);
    <T> void persistObject(T object);
    Recipe getRecipeWithNoDescription(User author);
    void deleteNotFinishedRecipes(long userId);
    boolean recipeExist(String recipeName);
    int nextRecipePhotoSequence(Recipe recipe);
}
