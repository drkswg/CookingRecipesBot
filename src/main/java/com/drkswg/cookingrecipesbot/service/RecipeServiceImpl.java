package com.drkswg.cookingrecipesbot.service;

import com.drkswg.cookingrecipesbot.dao.DAO;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeCategory;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.entity.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecipeServiceImpl implements RecipeService {
    DAO dao;

    @Autowired
    public RecipeServiceImpl(DAO dao) {
        this.dao = dao;
    }

    @Override
    public int nextRecipeStepPhotoSequence(RecipeStep recipeStep) { return dao.nextRecipeStepPhotoSequence(recipeStep); }

    @Override
    public <T> void mergeObject(T object) {
        dao.mergeObject(object);
    }

    @Override
    public int nextRecipePhotoSequence(Recipe recipe) { return dao.nextRecipePhotoSequence(recipe); }

    @Override
    public boolean recipeExist(String recipeName) { return dao.recipeExist(recipeName); }

    @Override
    public void deleteNotFinishedRecipes(long userId) { dao.deleteNotFinishedRecipes(userId); }

    @Override
    public Recipe getRecipeWithNoDescription(User author) { return dao.getRecipeWithNoDescription(author); }

    @Override
    public <T> void persistObject(T object) { dao.persistObject(object); }

    @Override
    public Recipe getBlankRecipe(User author) { return dao.getBlankRecipe(author); }

    @Override
    public RecipeCategory getRecipeCategory(String categoryName) {
        return dao.getRecipeCategory(categoryName);
    }

    @Override
    public Recipe addNewRecipeAuthorAndCategory(User author, RecipeCategory recipeCategory) {
        return dao.addNewRecipeAuthorAndCategory(author, recipeCategory);
    }

    @Override
    public User addUserIfNotExist(long id, String userName) {
        return dao.addUserIfNotExist(id, userName);
    }

    @Override
    public RecipeStep getNextStep(Recipe recipe, int step) {
        return dao.getNextStep(recipe, step);
    }

    @Override
    public Recipe getRecipe(String name) { return dao.getRecipe(name); }

    @Override
    public List<Recipe> getRecipes(String type) { return dao.getRecipes(type); }
}
