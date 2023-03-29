package com.drkswg.cookingrecipesbot.handler.processing;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Comparator;

public class AddRecipeStepName extends NonTypicalMessageProcessor {
    public AddRecipeStepName(ReplyKeyboardMaker replyKeyboardMaker,
                             InlineKeyboardMaker inlineKeyboardMaker,
                             RecipeService recipeService,
                             TelegramApiClient apiClient,
                             Message message,
                             Bot bot) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
    }

    @Override
    public SendMessage getMessage() {
        RecipeStep recipeStep = currentStep.getRecipe().getRecipeSteps()
                .stream()
                .max(Comparator.comparing(RecipeStep::getStep))
                .get();

        recipeStep.setName(messageText);
        recipeService.mergeObject(recipeStep);

        logCurrentStep(chatIdStr, String.format("add_step_name (%s)", recipeStep.getStep()), currentStep.getRecipe());
        currentStep.setStep("add_step_description");

        return new SendMessage(chatIdStr, "Введите описание шага:");
    }
}
