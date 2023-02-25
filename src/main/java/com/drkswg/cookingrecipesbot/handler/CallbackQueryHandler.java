package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.constants.ButtonNameEnum;
import com.drkswg.cookingrecipesbot.entity.*;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.model.UserStep;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQueryHandler {
    static final Logger LOGGER = LoggerFactory.getLogger(CallbackQueryHandler.class);
    static final String PATTERN_NOT_MATCHED = "Совпадений по паттерну не найдено";
    final RecipeService recipeService;
    final InlineKeyboardMaker inlineKeyboardMaker;
    Bot bot;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery, Bot bot) {
        this.bot = bot;
        String chatId = buttonQuery.getMessage().getChatId().toString();
        String data = buttonQuery.getData();

        for (ButtonNameEnum button : ButtonNameEnum.values()) {
            if (button.getType().equals("recipeCategory") & button.getName().equals(data)) {
                return createTempRecipe(buttonQuery, data);
            }
        }

        boolean recipeStep = !searchForMatch(data, "\\d+").equals(PATTERN_NOT_MATCHED);

        if (recipeStep) {
            return getNextStepMessage(chatId, data);
        } else {
            return getIngredientsMessage(chatId, data);
        }
    }

    private SendMessage createTempRecipe(CallbackQuery buttonQuery, String recipeCategoryName) {
        String chatId = buttonQuery.getMessage().getChatId().toString();
        long userId = buttonQuery.getFrom().getId();
        String userName = buttonQuery.getFrom().getUserName();
        User author = recipeService.addUserIfNotExist(userId, userName);
        RecipeCategory recipeCategory = recipeService.getRecipeCategory(recipeCategoryName);
        Recipe tempRecipe = recipeService.addNewRecipeAuthorAndCategory(author, recipeCategory);
        UserStep currentStep = bot.getUserStep(buttonQuery.getMessage().getChatId());
        LOGGER.info(String.format("""
                Добавление информации о шагах пользователя (chat_id): %s -> add_recipe_name, рецепт -> %s
                """,
                buttonQuery.getMessage().getChatId(), tempRecipe));
        currentStep.setRecipe(tempRecipe);
        currentStep.setStep("add_temp_recipe");

        return new SendMessage(chatId, "Введите название рецепта:");
    }

    private SendMessage getIngredientsMessage(String chatId, String data) {
        Recipe recipe = getRecipeIngredients(data);
        SendMessage message = new SendMessage(chatId, recipe.getDescription());
        RecipeStep recipeStep = recipeService.getNextStep(recipe, 1);
        message.setReplyMarkup(inlineKeyboardMaker.getNextStepKeyboard(recipeStep));
        recipe.getPhotos().stream()
                .sorted(Comparator.comparing(Photo::getSequence))
                .filter(photo -> photo.getRecipeStep() == null)
                .forEach(photo -> sendPicture(chatId, photo.getPath()));

        return message;
    }

    private SendMessage getNextStepMessage(String chatId, String recipeAndStep) {
        String recipeName = searchForMatch(recipeAndStep, "[а-яА-Я]+");
        int recipeStepNum = Integer.parseInt(searchForMatch(recipeAndStep, "\\d+"));
        Recipe recipe = recipeService.getRecipe(recipeName);
        int recipeStepsNumber = recipe.getRecipeSteps().size();
        SendMessage message = null;
        RecipeStep recipeStep = null;

        if (recipeStepsNumber >= recipeStepNum) {
            recipeStep = recipeService.getNextStep(recipe, recipeStepNum);
            message = new SendMessage(chatId, recipeStep.getDescription());
        }

        if (recipeStepsNumber > recipeStepNum) {
            message.setReplyMarkup(inlineKeyboardMaker.getNextStepKeyboard(recipeStep));
        }

        RecipeStep finalRecipeStep = recipeStep;
        recipeStep.getPhotos().stream()
                .sorted(Comparator.comparing(Photo::getSequence))
                .filter(photo -> photo.getRecipeStep().equals(finalRecipeStep))
                .forEach(photo -> sendPicture(chatId, photo.getPath()));

        return message;
    }

    private Recipe getRecipeIngredients(String recipeName) {
        return recipeService.getRecipe(recipeName);
    }

    private String searchForMatch(String text, String pattern) {
        Pattern patternObject = Pattern.compile(pattern);
        Matcher matcher = patternObject.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return PATTERN_NOT_MATCHED;
        }
    }

    private void sendPicture(String chatId, String filePath) {
        try {
            SendPhoto msg = new SendPhoto();
            msg.setChatId(chatId);
            msg.setPhoto(new InputFile(new File(filePath)));
            bot.execute(msg);
        } catch (Exception exc) {
            LOGGER.error("Ошибка при отправке фото рецепта", exc);
        }
    }
}
