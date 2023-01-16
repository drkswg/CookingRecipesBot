package com.drkswg.cookingrecipesbot.config;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.dao.RecipeDAO;
import com.drkswg.cookingrecipesbot.handler.CallbackQueryHandler;
import com.drkswg.cookingrecipesbot.handler.MessageHandler;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import com.drkswg.cookingrecipesbot.service.RecipeServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpringConfig {
    TelegramConfig telegramConfig;
    RecipeDAO recipeDAO;

    @Bean
    public RecipeService recipeService() { return new RecipeServiceImpl(recipeDAO); }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    public Bot springWebhookBot(SetWebhook setWebhook, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        Bot bot = new Bot(setWebhook, messageHandler, callbackQueryHandler);

        bot.setBotPath(telegramConfig.getWebhookPath());
        bot.setBotUsername(telegramConfig.getBotName());
        bot.setBotToken(telegramConfig.getBotToken());

        return bot;
    }
}
