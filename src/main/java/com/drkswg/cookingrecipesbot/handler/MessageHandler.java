package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.constants.BotMessagesEnum;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.entity.User;
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
                          RecipeService recipeService) {
        super(replyKeyboardMaker, inlineKeyboardMaker, recipeService);
    }

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        return switch (message.getText()) {
            case "/start", "/help" -> getHelpMessage(chatId);
            case "Супы" -> getRecipes(chatId, "Супы");
            case "Завтраки" -> getRecipes(chatId, "Завтраки");
            case "Горячие блюда" -> getRecipes(chatId, "Горячие блюда");
            case "Добавить рецепт" -> addRecipe(message);
            case "/test" -> test(message);
            case null -> null;
            default -> processNonTypicalMessage(message);
        };
    }

    private SendMessage test(Message message) {
        System.out.println(message.getPhoto());
        System.out.println("Файл: " + message.getDocument());
        System.out.println("Подпись: " + message.getCaption());

        return new SendMessage(message.getChatId().toString(), "test");
    }

    private SendMessage processNonTypicalMessage(Message message) {
        String chatId = message.getChatId().toString();
        long chatIdOrig = message.getChatId();
        long userId = message.getFrom().getId();
        String userName = message.getFrom().getUserName();
        User author = recipeService.addUserIfNotExist(userId, userName);
        Recipe blankRecipe = recipeService.getBlankRecipe(author);
        Recipe recipeWithNoDescription = recipeService.getRecipeWithNoDescription(author);
        UserStep currentStep = bot.getUserStep(chatIdOrig);
        SendMessage sendMessage;

        if (blankRecipe != null) {
            blankRecipe.setName(message.getText());
            recipeService.updateRecipe(blankRecipe);

            LOGGER.info(String.format("""
                Добавление информации о шагах пользователя (chat_id): %s -> add_recipe_description, рецепт -> %s
                """,
                    chatId, blankRecipe));
            currentStep.setStep("add_recipe_description");
            currentStep.setRecipe(blankRecipe);

            sendMessage = new SendMessage(
                    chatId,
                    String.format("Введите описание и ингридиенты для рецепта \"%s\":", blankRecipe.getName())
            );
        } else if (recipeWithNoDescription != null) {
            recipeWithNoDescription.setDescription(message.getText());
            recipeService.updateRecipe(recipeWithNoDescription);

            LOGGER.info(String.format("""
                Добавление информации о шагах пользователя (chat_id): %s -> add_recipe_description_photos, рецепт -> %s
                """,
                    chatId, recipeWithNoDescription));
            currentStep.setStep("add_recipe_description_photos");
            currentStep.setRecipe(recipeWithNoDescription);

            sendMessage = new SendMessage(
                    chatId,
                    String.format("""
                            Прикрепите фотографии к описанию рецепта (можно сразу несколько) "%s"
                            После окончания введите команду /add_steps
                            """,
                            recipeWithNoDescription.getName())
            );
        } else if (currentStep.getStep().equals("add_recipe_description_photos")) {


            return null;
        } else {
            sendMessage = new SendMessage(chatId, "Категория не выбрана! Выберите категорию:");
            sendMessage.setReplyMarkup(inlineKeyboardMaker.getCategoriesKeyboard());
        }

        return sendMessage;
    }

    private void attachPhotoToRecipe(Recipe recipe) {

    }

    private SendMessage addRecipe(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        UserStep currentStep = bot.getUserStep(message.getChatId());

        if (currentStep != null) {
            LOGGER.info(String.format("Очистка информации о шагах пользователя (chat_id): %s", chatId));
            bot.getUsersSteps().remove(currentStep);
        }

        recipeService.deleteNotFinishedRecipes(userId);

        LOGGER.info(String.format("Добавление информации о шагах пользователя (chat_id): %s -> choosing_category", chatId));
        bot.getUsersSteps().add(new UserStep(chatId, "choosing_category", null));
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Выберите категорию:");
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getCategoriesKeyboard());

        return sendMessage;
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
