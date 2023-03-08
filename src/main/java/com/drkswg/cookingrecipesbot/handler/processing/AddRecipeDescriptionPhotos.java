package com.drkswg.cookingrecipesbot.handler.processing;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;

public class AddRecipeDescriptionPhotos extends NonTypicalMessageProcessor {
    public AddRecipeDescriptionPhotos(ReplyKeyboardMaker replyKeyboardMaker,
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
            attachPhotoToRecipe(message);
        } catch (IOException ioEx) {
            LOGGER.error(String.format("Ошибка при добавлении фото к рецепту %s", currentStep.getRecipe()), ioEx);
        }

        return null;
    }
}
