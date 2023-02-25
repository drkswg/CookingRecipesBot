package com.drkswg.cookingrecipesbot.keyboard;

import com.drkswg.cookingrecipesbot.constants.ButtonNameEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(ButtonNameEnum.BREAKFASTS.getName()));
        firstRow.add(new KeyboardButton(ButtonNameEnum.SOUPS.getName()));
        firstRow.add(new KeyboardButton(ButtonNameEnum.DISHES.getName()));
        secondRow.add(new KeyboardButton(ButtonNameEnum.ADD_RECIPE.getName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(firstRow);
        keyboard.add(secondRow);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
