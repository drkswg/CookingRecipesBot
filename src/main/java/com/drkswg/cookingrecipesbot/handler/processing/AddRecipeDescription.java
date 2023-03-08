package com.drkswg.cookingrecipesbot.handler.processing;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class AddRecipeDescription extends NonTypicalMessageProcessor {
    public AddRecipeDescription(ReplyKeyboardMaker replyKeyboardMaker,
                                InlineKeyboardMaker inlineKeyboardMaker,
                                RecipeService recipeService,
                                TelegramApiClient apiClient,
                                Message message,
                                Bot bot) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
    }

    @Override
    public SendMessage getMessage() {
        Recipe recipeWithNoDescription = recipeService.getRecipeWithNoDescription(user);
        SendMessage sendMessage;

        if (recipeWithNoDescription != null) {
            recipeWithNoDescription.setDescription(message.getText());
            recipeService.persistObject(recipeWithNoDescription);

            logCurrentStep(chatIdStr, "add_recipe_description_photos", recipeWithNoDescription);

            currentStep.setStep("add_recipe_description_photos");
            currentStep.setRecipe(recipeWithNoDescription);

            sendMessage = new SendMessage(
                    chatIdStr,
                    String.format("""
                            Прикрепите фотографии к описанию рецепта (можно сразу несколько) "%s"
                            После окончания введите команду /add_steps
                            """,
                            recipeWithNoDescription.getName())
            );
        } else {
            sendMessage = new SendMessage(chatIdStr, "Рецепт уже существует! Введите другое название:");
        }

        return sendMessage;
    }
}
