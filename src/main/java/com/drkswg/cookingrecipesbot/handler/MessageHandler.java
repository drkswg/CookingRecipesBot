package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.constants.BotMessagesEnum;
import com.drkswg.cookingrecipesbot.entity.RecipeStep;
import com.drkswg.cookingrecipesbot.handler.processing.*;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.model.UserStep;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler extends Handler {
    public MessageHandler(ReplyKeyboardMaker replyKeyboardMaker,
                          InlineKeyboardMaker inlineKeyboardMaker,
                          RecipeService recipeService,
                          TelegramApiClient apiClient) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient);
    }

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        return switch (message.getText()) {
            case "/start", "/help" -> getHelpMessage(chatId);
            case "Супы" -> getRecipes(chatId, "Супы");
            case "Завтраки" -> getRecipes(chatId, "Завтраки");
            case "Горячие блюда" -> getRecipes(chatId, "Горячие блюда");
            case "Добавить рецепт" -> addRecipe(message);
            case "/add_steps", "/next_step" -> addRecipeStep(message);
            case "/done" -> new SendMessage(
                    chatId, "Рецепт добавлен! Вы можете найти его в соответствующем разделе"
            );
            case null -> processNonTypicalMessage(message);
            default -> processNonTypicalMessage(message);
        };
    }

    private SendMessage addRecipeStep(Message message) {
        long chatId = message.getChatId();
        String chatIdStr = String.valueOf(chatId);
        UserStep currentStep = bot.getUserStep(chatId);
        int recipeStepNum = currentStep.getRecipe().getRecipeSteps().size() + 1;
        String text = recipeStepNum == 1 ? """
                Теперь добавим пошаговый план рецепта.
                Напишите название первого шага, например
                "Приготовление бульона":
                """ : "Введите название шага";
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        RecipeStep recipeStep = new RecipeStep();
        recipeStep.setRecipe(currentStep.getRecipe());
        recipeStep.setStep(recipeStepNum);
        recipeService.persistObject(recipeStep);
        currentStep.getRecipe().getRecipeSteps().add(recipeStep);

        logCurrentStep(chatIdStr, String.format("add_step_name (%s)", recipeStepNum), currentStep.getRecipe());
        currentStep.setStep("add_step_name");

        return sendMessage;
    }

    private NonTypicalMessageProcessor getCurrentProcessStep(Message message) {
        String currentStep = bot.getUserStep(message.getChatId()).getStep();

        return switch (currentStep) {
            case "add_recipe_name" -> new AddRecipeName(
                    replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
            case "add_recipe_description" -> new AddRecipeDescription(
                    replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
            case "add_recipe_description_photos" -> new AddRecipeDescriptionPhotos(
                    replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
            case "add_step_name" -> new AddRecipeStepName(
                    replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
            case "add_step_description" -> new AddRecipeStepDescription(
                    replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
            case "add_step_photo" -> new AddRecipeStepPhoto(
                    replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient, message, bot);
            default -> null;
        };
    }

    private SendMessage processNonTypicalMessage(Message message) {
        NonTypicalMessageProcessor processor = getCurrentProcessStep(message);

        return processor.getMessage();
    }

    private SendMessage addRecipe(Message message) {
        UserStep currentStep = bot.getUserStep(message.getChatId());

        return recipeCategoryPick(currentStep, message, false);
    }

    private SendMessage getRecipes(String chatId, String type) {
        SendMessage sendMessage = new SendMessage(chatId, "Выбирайте:");
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getRecipesKeyboard(recipeService.getRecipes(type)));

        return sendMessage;
    }

    private SendMessage getHelpMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessagesEnum.HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());

        return sendMessage;
    }
}
