package com.drkswg.cookingrecipesbot.dao;

import com.drkswg.cookingrecipesbot.entity.*;

import java.util.List;

public interface DAO {
    List<Recipe> getRecipes(String type);
    Recipe getRecipe(String name);
    RecipeStep getNextStep(Recipe recipe, int step);
    User addUserIfNotExist(long id, String userName);
    RecipeCategory getRecipeCategory(String categoryName);
    Recipe addNewRecipeAuthorAndCategory(User author, RecipeCategory recipeCategory);
    Recipe getBlankRecipe(User author);
    <T> void persistObject(T object);
    <T> void mergeObject(T object);
    Recipe getRecipeWithNoDescription(User author);
    void deleteNotFinishedRecipes(long userId);
    boolean recipeExist(String recipeName);
    int nextRecipePhotoSequence(Recipe recipe);
    int nextRecipeStepPhotoSequence(RecipeStep recipeStep);
}
