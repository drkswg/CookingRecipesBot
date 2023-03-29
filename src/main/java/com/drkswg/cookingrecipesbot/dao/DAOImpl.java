package com.drkswg.cookingrecipesbot.dao;

import com.drkswg.cookingrecipesbot.entity.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;


@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DAOImpl implements DAO {
    static final Logger LOGGER = LoggerFactory.getLogger(DAOImpl.class);
    EntityManager entityManager;

    @Autowired
    public DAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public <T> void mergeObject(T object) {
        entityManager.merge(object);
    }

    @Override
    public int nextRecipeStepPhotoSequence(RecipeStep recipeStep) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Integer> query = currentSession.createQuery("""
                select max(sequence) from Photo
                where recipe_id = :recipe_id
                and recipe_step_id = :recipe_step_id
                """, Integer.class)
                .setParameter("recipe_id", recipeStep.getRecipe().getId())
                .setParameter("recipe_step_id", recipeStep.getId());
        int nextSequence = 1;

        try {
            int tmpSequence = query.getSingleResult() == null ? 0 : query.getSingleResult();
            nextSequence = tmpSequence + 1;
        } catch (NoResultException ignored) {}

        return nextSequence;
    }

    @Override
    public int nextRecipePhotoSequence(Recipe recipe) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Integer> query = currentSession.createQuery("select max(sequence) from Photo " +
                "where recipe_id = :recipe_id", Integer.class);
        query.setParameter("recipe_id", recipe.getId());
        int nextSequence = 1;

        try {
            int tmpSequence = query.getSingleResult() == null ? 0 : query.getSingleResult();
            nextSequence = tmpSequence + 1;
        } catch (NoResultException ignored) {}

        return nextSequence;
    }

    @Override
    public void deleteNotFinishedRecipes(long userId) {
        LOGGER.info(String.format("Очистка незаконченных рецептов пользователя: %s", userId));
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.createQuery("""
                        delete from Recipe where description is null 
                        and added_by = :user_id
                        """)
                .setParameter("user_id", userId)
                .executeUpdate();
    }

    @Override
    public <T> void persistObject(T object) {
        entityManager.persist(object);
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
    public boolean recipeExist(String recipeName) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Recipe> query = currentSession.createQuery("from Recipe where name = :name", Recipe.class)
                .setParameter("name", recipeName);

        try {
            query.getSingleResult();

            return true;
        } catch (NonUniqueResultException sizEx) {
            return true;
        } catch (NoResultException noResExc) {
            return false;
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
                .createQuery("""
                        SELECT r
                        FROM Recipe r
                        WHERE r.name IS NOT NULL
                          AND r.description IS NOT NULL
                          and r.recipeCategory.id = :recipeCategoryId
                          AND EXISTS (
                            SELECT 1
                            FROM RecipeStep s
                            WHERE s.recipe = r
                              AND s.name IS NOT NULL
                              AND s.description IS NOT NULL
                              )
                        """, Recipe.class)
                .setParameter("recipeCategoryId", recipeCategoryId);

        return query.getResultList();
    }
}
