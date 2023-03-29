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

public class AddRecipeStepDescription extends NonTypicalMessageProcessor {
    public AddRecipeStepDescription(ReplyKeyboardMaker replyKeyboardMaker,
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

        recipeStep.setDescription(messageText);
        recipeService.mergeObject(recipeStep);

        logCurrentStep(chatIdStr, String.format("add_step_photo (%s)",
                recipeStep.getStep()), currentStep.getRecipe());
        currentStep.setStep("add_step_photo");

        return new SendMessage(
                chatIdStr,
                String.format("""
                            Прикрепите фотографии к описанию шага рецепта (можно сразу несколько) "%s"
                            После окончания введите команду /next_step для добавления следующего шага
                            или /done для завершения добавления рецепта
                            """,
                        recipeStep.getName())
        );
    }
}
