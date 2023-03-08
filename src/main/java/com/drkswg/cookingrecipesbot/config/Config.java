package com.drkswg.cookingrecipesbot.config;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.dao.RecipeDAO;
import com.drkswg.cookingrecipesbot.handler.CallbackQueryHandler;
import com.drkswg.cookingrecipesbot.handler.MessageHandler;
import com.drkswg.cookingrecipesbot.model.UserStep;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import com.drkswg.cookingrecipesbot.service.RecipeServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Config {
    @Value("${telegram.webhook-path}")
    String webhookPath;
    @Value("${telegram.bot-name}")
    String botName;
    @Value("${telegram.bot-token}")
    String botToken;
    final RecipeDAO recipeDAO;

    public Config(RecipeDAO recipeDAO) { this.recipeDAO = recipeDAO; }

    @Bean
    public TelegramApiClient apiClient() { return new TelegramApiClient(new RestTemplate()); }

    @Bean
    @Scope("singleton")
    public Set<UserStep> usersSteps() { return ConcurrentHashMap.newKeySet(); }

    @Bean
    public RecipeService recipeService() { return new RecipeServiceImpl(recipeDAO); }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(webhookPath).build();
    }

    @Bean
    public Bot springWebhookBot(SetWebhook setWebhook,
                                MessageHandler messageHandler,
                                CallbackQueryHandler callbackQueryHandler,
                                Set<UserStep> usersSteps) {
        Bot bot = new Bot(setWebhook, messageHandler, callbackQueryHandler, usersSteps);

        bot.setBotPath(webhookPath);
        bot.setBotUsername(botName);
        bot.setBotToken(botToken);

        return bot;
    }
}
