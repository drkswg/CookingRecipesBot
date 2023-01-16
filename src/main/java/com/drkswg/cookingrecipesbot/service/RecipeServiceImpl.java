package com.drkswg.cookingrecipesbot.service;

import com.drkswg.cookingrecipesbot.dao.RecipeDAO;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
