package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.constants.BotMessagesEnum;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {
    ReplyKeyboardMaker replyKeyboardMaker;
    InlineKeyboardMaker inlineKeyboardMaker;
    RecipeService recipeService;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        return switch (message.getText()) {
            case "/start", "/help" -> getHelpMessage(chatId);
            case null -> throw new IllegalArgumentException();
            case "Супы" -> getRecipes(chatId, "Супы");
            case "Завтраки" -> getRecipes(chatId, "Завтраки");
            case "Горячие блюда" -> getRecipes(chatId, "Горячие блюда");
            default -> new SendMessage(chatId, "Заходи попозже!");
        };
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
