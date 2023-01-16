package com.drkswg.cookingrecipesbot.dao;

import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDAOImpl implements RecipeDAO {
    EntityManager entityManager;

    @Autowired
    public RecipeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RecipeStep getNextStep(Recipe recipe, int step) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<RecipeStep> query = currentSession
                .createQuery("from RecipeStep where step = :step and recipe_fk = :recipe_fk", RecipeStep.class)
                .setParameter("step", step)
                .setParameter("recipe_fk", recipe.getId());

        return query.getSingleResult();
    }

    @Override
    public Recipe getRecipe(String name) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Recipe> query = currentSession
                .createQuery("from Recipe where name = :name", Recipe.class)
                .setParameter("name", name);

        return query.getSingleResult();
    }

    @Override
    public List<Recipe> getRecipes(String type) {
        Session currentSession = entityManager.unwrap(Session.class);
        int recipeCategoryId = currentSession
                .createQuery("select id from RecipeCategory where name = :type", Integer.class)
                .setParameter("type", type)
                .getSingleResult();
        Query<Recipe> query = currentSession
                .createQuery("from Recipe where rc_fk = :recipeCategoryId order by id", Recipe.class)
                .setParameter("recipeCategoryId", recipeCategoryId);

        return query.getResultList();
    }
}
