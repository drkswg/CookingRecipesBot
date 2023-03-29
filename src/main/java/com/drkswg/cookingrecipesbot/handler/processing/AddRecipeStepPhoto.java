package com.drkswg.cookingrecipesbot.handler.processing;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.Comparator;

public class AddRecipeStepPhoto extends NonTypicalMessageProcessor {

    public AddRecipeStepPhoto(ReplyKeyboardMaker replyKeyboardMaker,
                              InlineKeyboardMaker inlineKeyboardMaker,
                              RecipeService recipeService,
                              TelegramApiClient apiClient,
                              Message message,
                              Bot bot) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
    }

    @Override
    public SendMessage getMessage() {
        try {
            attachPhoto(message);
        } catch (IOException ioEx) {
            LOGGER.error(String.format("Ошибка при добавлении фото к шагу %s рецепта %s",
                    currentStep.getRecipe().getRecipeSteps()
                            .stream()
                            .max(Comparator.comparing(RecipeStep::getStep))
                            .orElse(null),
                    currentStep.getRecipe()), ioEx);
        }

        return null;
    }
}
