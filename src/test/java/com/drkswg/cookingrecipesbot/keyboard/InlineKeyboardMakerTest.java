package com.drkswg.cookingrecipesbot.keyboard;

import com.drkswg.cookingrecipesbot.constants.ButtonNameEnum;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InlineKeyboardMakerTest {
    private InlineKeyboardMaker inlineKeyboardMaker;

    @Mock
    private RecipeStep recipeStep;

    @Mock
    private Recipe recipe;

    @BeforeEach
    public void setUp() {
        inlineKeyboardMaker = new InlineKeyboardMaker();
    }

    @Test
    public void testGetCategoriesKeyboard() {
        InlineKeyboardMarkup markup = inlineKeyboardMaker.getCategoriesKeyboard();

        assertNotNull(markup);
        assertEquals(ButtonNameEnum.values().length - 1, markup.getKeyboard().size());
        markup.getKeyboard().forEach(buttonRow -> {
            assertEquals(1, buttonRow.size());
            assertTrue(Arrays.stream(ButtonNameEnum.values()).anyMatch(
                    button -> buttonRow.get(0).getText().equals(button.getName())));
        });
    }

    @Test
    public void testGetFirstStepKeyboard() {
        when(recipeStep.getStep()).thenReturn(1);
        when(recipeStep.getRecipe()).thenReturn(recipe);
        when(recipe.getName()).thenReturn("Test Recipe");

        InlineKeyboardMarkup markup = inlineKeyboardMaker.getFirstStepKeyboard(recipeStep);

        assertNotNull(markup);
        assertEquals(1, markup.getKeyboard().size());
        assertEquals("Далее", markup.getKeyboard().get(0).get(0).getText());
        assertEquals("Test Recipe 1", markup.getKeyboard().get(0).get(0).getCallbackData());
    }

    @Test
    public void testGetNextStepKeyboard() {
        when(recipeStep.getStep()).thenReturn(1);
        when(recipeStep.getRecipe()).thenReturn(recipe);
        when(recipe.getName()).thenReturn("Test Recipe");

        InlineKeyboardMarkup markup = inlineKeyboardMaker.getNextStepKeyboard(recipeStep);

        assertNotNull(markup);
        assertEquals(1, markup.getKeyboard().size());
        assertEquals("Далее", markup.getKeyboard().get(0).get(0).getText());
        assertEquals("Test Recipe 2", markup.getKeyboard().get(0).get(0).getCallbackData());
    }

    @Test
    public void testGetRecipesKeyboard() {
        List<Recipe> recipes = List.of(
                mock(Recipe.class),
                mock(Recipe.class),
                mock(Recipe.class)
        );

        when(recipes.get(0).getName()).thenReturn("Recipe 1");
        when(recipes.get(1).getName()).thenReturn("Recipe 2");
        when(recipes.get(2).getName()).thenReturn("Recipe 3");

        InlineKeyboardMarkup markup = inlineKeyboardMaker.getRecipesKeyboard(recipes);

        assertNotNull(markup);
        assertEquals(3, markup.getKeyboard().size());
        for (int i = 0; i < recipes.size(); i++) {
            assertEquals(recipes.get(i).getName(), markup.getKeyboard().get(i).get(0).getText());
            assertEquals(recipes.get(i).getName(), markup.getKeyboard().get(i).get(0).getCallbackData());
        }
    }
}
