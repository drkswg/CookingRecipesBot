package com.drkswg.cookingrecipesbot;

import com.drkswg.cookingrecipesbot.handler.CallbackQueryHandler;
import com.drkswg.cookingrecipesbot.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.File;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public Bot(SetWebhook setWebhook, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(), "Ошибочка вышла, вооот");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            return callbackQueryHandler.processCallbackQuery(callbackQuery, this);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }

        return null;
    }
}
