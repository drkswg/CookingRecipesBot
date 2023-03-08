package com.drkswg.cookingrecipesbot.handler;

import com.drkswg.cookingrecipesbot.Bot;
import com.drkswg.cookingrecipesbot.api.TelegramApiClient;
import com.drkswg.cookingrecipesbot.entity.Photo;
import com.drkswg.cookingrecipesbot.entity.Recipe;
import com.drkswg.cookingrecipesbot.keyboard.InlineKeyboardMaker;
import com.drkswg.cookingrecipesbot.keyboard.ReplyKeyboardMaker;
import com.drkswg.cookingrecipesbot.model.MimeDetector;
import com.drkswg.cookingrecipesbot.model.Translit;
import com.drkswg.cookingrecipesbot.model.UserStep;
import com.drkswg.cookingrecipesbot.service.RecipeService;
import exception.DocumentNotAttachedException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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
    final TelegramApiClient apiClient;

    public Handler(ReplyKeyboardMaker replyKeyboardMaker,
                   InlineKeyboardMaker inlineKeyboardMaker,
                   RecipeService recipeService,
                   TelegramApiClient apiClient) {
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.recipeService = recipeService;
        this.apiClient = apiClient;
    }

    protected void attachPhotoToRecipe(Message message) throws IOException {
        Document document = message.getDocument();
        List<PhotoSize> photos = message.getPhoto();
        String fileId;
        String extension;

        if (document != null) {
            fileId = document.getFileId();
            extension = "." + FilenameUtils.getExtension(document.getFileName());
        } else if (message.getPhoto() != null
                & message.getPhoto().size() > 0) {
            PhotoSize maxPhotoSize = photos.stream().max(Comparator.comparing(PhotoSize::getHeight)).get();
            fileId = maxPhotoSize.getFileId();
            File tmpFile = apiClient.getDocumentFile(fileId,
                    "photos/tmp" + File.separator + UUID.randomUUID());
            InputStream inputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(tmpFile));
            extension = URLConnection.guessContentTypeFromStream(inputStream);
            inputStream.close();
            tmpFile.delete();
            extension = MimeDetector.getExtension(extension);
        } else {
            throw new DocumentNotAttachedException("Recipe photo not attached");
        }

        UserStep currentStep = bot.getUserStep(message.getChatId());
        Recipe currentRecipe = currentStep.getRecipe();
        String translitedRecipeName = Translit.convert(currentRecipe.getName());
        Path recipePhotoPath = Paths.get("photos"
                + File.separator
                + translitedRecipeName);
        int photoSequence = recipeService.nextRecipePhotoSequence(currentRecipe);
        String fileName = recipePhotoPath
                + File.separator
                + translitedRecipeName
                + "_"
                + photoSequence
                + extension;

        if (!Files.exists(recipePhotoPath)) {
            Files.createDirectory(recipePhotoPath);
        }

        File photo = apiClient.getDocumentFile(fileId, fileName);

        LOGGER.info("Сохранение файла: " + photo.getAbsolutePath());

        Photo recipePhoto = new Photo();
        recipePhoto.setPath(photo.getAbsolutePath());
        recipePhoto.setSequence(photoSequence);
        recipePhoto.setRecipe(currentRecipe);

        LOGGER.info("Сохранение объекта фото в БД: " + recipePhoto);
        recipeService.persistObject(recipePhoto);
    }

    protected void logCurrentStep(String chatId, String step, Recipe recipe) {
        LOGGER.info(String.format("""
                Добавление информации о шагах пользователя (chat_id): %s -> %s, рецепт -> %s
                """,
                chatId, step, recipe));
    }

    protected SendMessage recipeCategoryPick(UserStep currentStep, Message message, boolean errorCase) {
        String messageText = errorCase ?
                "Что-то пошло не так! Давайте попробуем заново. Выберите категорию:"
                : "Выберите категорию:";

        if (currentStep != null) {
            LOGGER.info(String.format("Очистка информации о шагах пользователя (chat_id): %s",
                    message.getChatId().toString()));
            bot.getUsersSteps().remove(currentStep);
        }

        recipeService.deleteNotFinishedRecipes(message.getFrom().getId());

        LOGGER.info(String.format("Добавление информации о шагах пользователя (chat_id): %s -> choosing_category",
                message.getChatId().toString()));
        bot.getUsersSteps().add(new UserStep(message.getChatId(), "choosing_category", null));
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), messageText);
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getCategoriesKeyboard());

        return sendMessage;
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
