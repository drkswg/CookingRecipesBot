package com.drkswg.cookingrecipesbot.service;

import com.drkswg.cookingrecipesbot.dao.RecipeDAO;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeCategory;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.entity.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.A;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeServiceImpl implements RecipeService {
    RecipeDAO DAO;

    @Autowired
    public RecipeServiceImpl(RecipeDAO DAO) {
        this.DAO = DAO;
    }

    @Override
    @Transactional
    public int nextRecipePhotoSequence(Recipe recipe) { return DAO.nextRecipePhotoSequence(recipe); }

    @Override
    @Transactional
    public boolean recipeExist(String recipeName) { return DAO.recipeExist(recipeName); }

    @Override
    @Transactional
    public void deleteNotFinishedRecipes(long userId) { DAO.deleteNotFinishedRecipes(userId); }

    @Override
    @Transactional
    public Recipe getRecipeWithNoDescription(User author) { return DAO.getRecipeWithNoDescription(author); }

    @Override
    @Transactional
    public <T> void persistObject(T object) { DAO.persistObject(object); }

    @Override
    @Transactional
    public Recipe getBlankRecipe(User author) { return DAO.getBlankRecipe(author); }

    @Override
    @Transactional
    public RecipeCategory getRecipeCategory(String categoryName) {
        return DAO.getRecipeCategory(categoryName);
    }

    @Override
    @Transactional
    public Recipe addNewRecipeAuthorAndCategory(User author, RecipeCategory recipeCategory) {
        return DAO.addNewRecipeAuthorAndCategory(author, recipeCategory);
    }

    @Override
    @Transactional
    public User addUserIfNotExist(long id, String userName) {
        return DAO.addUserIfNotExist(id, userName);
    }

    @Override
    @Transactional
    public RecipeStep getNextStep(Recipe recipe, int step) {
        return DAO.getNextStep(recipe, step);
    }

    @Override
    @Transactional
    public Recipe getRecipe(String name) { return DAO.getRecipe(name); }

    @Override
    @Transactional
    public List<Recipe> getRecipes(String type) { return DAO.getRecipes(type); }
}
