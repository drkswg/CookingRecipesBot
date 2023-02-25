package com.drkswg.cookingrecipesbot.dao;

import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeCategory;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.entity.User;
import com.drkswg.cookingrecipesbot.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;


@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDAOImpl implements RecipeDAO {
    static final Logger LOGGER = LoggerFactory.getLogger(RecipeDAOImpl.class);
    EntityManager entityManager;

    @Autowired
    public RecipeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteNotFinishedRecipes(long userId) {
        LOGGER.info(String.format("Очистка незаконченных рецептов пользователя: %s", userId));
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.createQuery("delete from Recipe where description is null " +
                        "and added_by = :user_id")
                .setParameter("user_id", userId)
                .executeUpdate();
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        entityManager.persist(recipe);
    }

    @Override
    public Recipe getRecipeWithNoDescription(User author) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Recipe> query = currentSession.createQuery("from Recipe where added_by = :user_id " +
                        "and name is not null " +
                        "and description is null", Recipe.class)
                .setParameter("user_id", author.getId());

        try {
            return query.getSingleResult();
        } catch (NoResultException noResExc) {
            return null;
        }
    }

    @Override
    public Recipe getBlankRecipe(User author) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Recipe> query = currentSession.createQuery("from Recipe where added_by = :user_id " +
                        "and name is null " +
                        "and description is null", Recipe.class)
                .setParameter("user_id", author.getId());

        try {
            return query.getSingleResult();
        } catch (NoResultException noResExc) {
            return null;
        }
    }

    @Override
    public Recipe addNewRecipeAuthorAndCategory(User author, RecipeCategory recipeCategory) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Recipe> query = currentSession.createQuery("from Recipe where added_by = :user_id " +
                "and rc_fk = :recipe_category_id " +
                "and name is null " +
                "and description is null", Recipe.class)
                .setParameter("user_id", author.getId())
                .setParameter("recipe_category_id", recipeCategory.getId());
        Recipe tempRecipe;

        try {
            tempRecipe = query.getSingleResult();
        } catch (NoResultException noResExc) {
            tempRecipe = new Recipe();
            tempRecipe.setRecipeCategory(recipeCategory);
            tempRecipe.setUser(author);

            entityManager.persist(tempRecipe);
        }

        return tempRecipe;
    }

    @Override
    public RecipeCategory getRecipeCategory(String categoryName) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<RecipeCategory> query = currentSession
                .createQuery("from RecipeCategory where name = :name", RecipeCategory.class)
                .setParameter("name", categoryName);

        return query.getSingleResult();
    }

    @Override
    public User addUserIfNotExist(long id, String userName) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<User> query = currentSession.createQuery("from User where user_id = :user_id", User.class)
                .setParameter("user_id", id);
        User user;

        try {
            user = query.getSingleResult();
        } catch (NoResultException noResExc) {
            user = new User();
            user.setId(id);
            user.setName(userName);

            entityManager.persist(user);
        }

        return user;
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
