package com.drkswg.cookingrecipesbot.keyboard;

import com.drkswg.cookingrecipesbot.constants.ButtonNameEnum;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InlineKeyboardMaker {
    public InlineKeyboardMarkup getCategoriesKeyboard() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Arrays.stream(ButtonNameEnum.values())
                .filter(button -> button.getType().equals("recipeCategory"))
                .forEachOrdered(button -> rowList.add(getButton(button.getName(), button.getName())));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getFirstStepKeyboard(RecipeStep recipeStep) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int step = recipeStep.getStep();
        rowList.add(getButton("Далее",
                recipeStep.getRecipe().getName()
                        + " "
                        + step));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getNextStepKeyboard(RecipeStep recipeStep) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int step = recipeStep.getStep() + 1;
        rowList.add(getButton("Далее",
                recipeStep.getRecipe().getName()
                        + " "
                        + step));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getRecipesKeyboard(List<Recipe> recipes) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        recipes.forEach(recipe -> rowList.add(getButton(recipe.getName(), recipe.getName())));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);

        return keyboardButtonsRow;
    }
}
