package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Handler {
    static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);
    static final String PATTERN_NOT_MATCHED = "Совпадений по паттерну не найдено";
    Bot bot;
    final ReplyKeyboardMaker replyKeyboardMaker;
    final InlineKeyboardMaker inlineKeyboardMaker;
    final RecipeService recipeService;

    public Handler(ReplyKeyboardMaker replyKeyboardMaker,
                   InlineKeyboardMaker inlineKeyboardMaker,
                   RecipeService recipeService) {
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.recipeService = recipeService;
    }

    protected String searchForMatch(String text, String pattern) {
        Pattern patternObject = Pattern.compile(pattern);
        Matcher matcher = patternObject.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return PATTERN_NOT_MATCHED;
        }
    }

    protected void sendPicture(String chatId, String filePath) {
        try {
            SendPhoto msg = new SendPhoto();
            msg.setChatId(chatId);
            msg.setPhoto(new InputFile(new File(filePath)));
            bot.execute(msg);
        } catch (Exception exc) {
            LOGGER.error("Ошибка при отправке фото рецепта", exc);
        }
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
