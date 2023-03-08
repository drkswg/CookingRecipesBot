package com.drkswg.cookingrecipesbot.handler.processing;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.entity.User;
import com.drkswg.cookingrecipesbot.handler.Handler;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.model.UserStep;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class NonTypicalMessageProcessor extends Handler {
    static final Logger LOGGER = LoggerFactory.getLogger(NonTypicalMessageProcessor.class);
    UserStep currentStep;
    Message message;
    String messageText;
    String chatIdStr;
    long chatId;
    User user;

    public NonTypicalMessageProcessor(ReplyKeyboardMaker replyKeyboardMaker,
                                      InlineKeyboardMaker inlineKeyboardMaker,
                                      RecipeService recipeService,
                                      TelegramApiClient apiClient,
                                      Message message,
                                      Bot bot) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService, apiClient);
        this.message = message;
        this.messageText = message.getText();
        this.chatIdStr = message.getChatId().toString();
        this.chatId = message.getChatId();
        this.user = recipeService.addUserIfNotExist(message.getFrom().getId(),
                message.getFrom().getUserName());
        super.bot = bot;
        this.currentStep = bot.getUserStep(chatId);
    }

    public abstract SendMessage getMessage();
}
