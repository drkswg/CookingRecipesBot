package com.drkswg.cookingrecipesbot.handler.processing;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class AddRecipeName extends NonTypicalMessageProcessor{
    public AddRecipeName(ReplyKeyboardMaker replyKeyboardMaker,
                         InlineKeyboardMaker inlineKeyboardMaker,
                         RecipeService recipeService,
                         TelegramApiClient apiClient,
                         Message message,
                         Bot bot) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
    }

    @Override
    public SendMessage getMessage() {
        Recipe blankRecipe = recipeService.getBlankRecipe(user);
        SendMessage sendMessage;

        if (blankRecipe != null) {
            if (!recipeService.recipeExist(messageText)) {
                blankRecipe.setName(message.getText());
                recipeService.persistObject(blankRecipe);

                logCurrentStep(chatIdStr, "add_recipe_description", blankRecipe);

                currentStep.setStep("add_recipe_description");
                currentStep.setRecipe(blankRecipe);

                sendMessage = new SendMessage(
                        chatIdStr,
                        String.format("Введите описание и ингридиенты для рецепта \"%s\":", blankRecipe.getName()));
            } else {
                sendMessage = new SendMessage(chatIdStr, "Рецепт уже существует! Введите другое название:");
            }
        } else {
            return recipeCategoryPick(currentStep, message, true);
        }

        return sendMessage;
    }
}
