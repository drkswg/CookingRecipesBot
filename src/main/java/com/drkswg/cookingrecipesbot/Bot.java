package com.drkswg.cookingrecipesbot;

import com.drkswg.cookingrecipesbot.handler.CallbackQueryHandler;
import com.drkswg.cookingrecipesbot.handler.MessageHandler;
import com.drkswg.cookingrecipesbot.model.UserStep;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;
    Set<UserStep> usersSteps;

    public Bot(SetWebhook setWebhook,
               MessageHandler messageHandler,
               CallbackQueryHandler callbackQueryHandler,
               Set<UserStep> usersSteps) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.usersSteps = usersSteps;
        this.messageHandler.setBot(this);
        this.callbackQueryHandler.setBot(this);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();

            try {
                return new SendMessage(update.getMessage().getChatId().toString(), "Ошибочка вышла, вооот");
            } catch (NullPointerException nullEx) {
                return new SendMessage();
            }
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }

        return null;
    }

    public UserStep getUserStep(long chatId) {
        return usersSteps.stream()
                .filter(userStep -> userStep.getChatId() == chatId)
                .findAny()
                .orElse(null);
    }

    public Set<UserStep> getUsersSteps() {
        return usersSteps;
    }
}
