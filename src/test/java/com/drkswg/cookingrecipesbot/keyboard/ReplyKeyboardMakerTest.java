package com.drkswg.cookingrecipesbot.keyboard;

import com.drkswg.cookingrecipesbot.constants.ButtonNameEnum;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReplyKeyboardMakerTest {
    @Test
    void testGetMainMenuKeyboard() {
        ReplyKeyboardMaker replyKeyboardMaker = new ReplyKeyboardMaker();

        ReplyKeyboardMarkup replyKeyboardMarkup = replyKeyboardMaker.getMainMenuKeyboard();
        assertNotNull(replyKeyboardMarkup);

        List<KeyboardRow> keyboard = replyKeyboardMarkup.getKeyboard();

        KeyboardRow firstRow = keyboard.get(0);
        assertEquals(3, firstRow.size());
        assertEquals(ButtonNameEnum.BREAKFASTS.getName(), firstRow.get(0).getText());
        assertEquals(ButtonNameEnum.SOUPS.getName(), firstRow.get(1).getText());
        assertEquals(ButtonNameEnum.DISHES.getName(), firstRow.get(2).getText());

        KeyboardRow secondRow = keyboard.get(1);
        assertEquals(1, secondRow.size());
        assertEquals(ButtonNameEnum.ADD_RECIPE.getName(), secondRow.get(0).getText());

        assertEquals(true, replyKeyboardMarkup.getSelective());
        assertEquals(true, replyKeyboardMarkup.getResizeKeyboard());
        assertEquals(false, replyKeyboardMarkup.getOneTimeKeyboard());
    }
}
