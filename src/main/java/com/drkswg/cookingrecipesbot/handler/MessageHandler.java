package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.constants.BotMessagesEnum;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.User;
import com.drkswg.cookingrecipesbot.handler.processing.AddRecipeDescription;
import com.drkswg.cookingrecipesbot.handler.processing.AddRecipeDescriptionPhotos;
import com.drkswg.cookingrecipesbot.handler.processing.AddRecipeName;
import com.drkswg.cookingrecipesbot.handler.processing.NonTypicalMessageProcessor;
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

import java.io.IOException;

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
            case "/add_steps" -> null;
            case "/test" -> test(message);
            case null -> processNonTypicalMessage(message);
//            case null -> null;
//            case null ->  test(message);
            default -> processNonTypicalMessage(message);
        };
    }

    private SendMessage test(Message message) {
        System.out.println(message.getPhoto());
        System.out.println("Файл: " + message.getDocument());
        System.out.println("Подпись: " + message.getCaption());

//        try {
//            attachPhotoToRecipe(message);
//        } catch (IOException ioEx) {
//            ioEx.printStackTrace();
//        }

        return new SendMessage(message.getChatId().toString(), "test");
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
