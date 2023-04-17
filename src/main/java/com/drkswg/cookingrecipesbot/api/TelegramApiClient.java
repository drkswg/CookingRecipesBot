package com.drkswg.cookingrecipesbot.api;

import com.drkswg.cookingrecipesbot.exception.TelegramFileNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TelegramApiClient {
    static final Logger LOGGER = LoggerFactory.getLogger(TelegramApiClient.class);
    @Value("${telegram.api-url}")
    String url;
    @Value("${telegram.bot-token}")
    String botToken;
    final RestTemplate restTemplate;

    public File getDocumentFile(String fileId, String filePath) {
        try {
            return restTemplate.execute(
                    Objects.requireNonNull(getDocumentTelegramFileUrl(fileId)),
                    HttpMethod.GET,
                    null,
                    clientHttpResponse -> {
                        File file = new File(filePath);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        StreamUtils.copy(clientHttpResponse.getBody(), fileOutputStream);
                        fileOutputStream.close();

                        return file;
                    });
        } catch (Exception exc) {
            LOGGER.error("Ошибка при получении загруженного файла", exc);
            throw new TelegramFileNotFoundException();
        }
    }

    private String getDocumentTelegramFileUrl(String fileId) {
        try {
            ResponseEntity<ApiResponse<org.telegram.telegrambots.meta.api.objects.File>> response = restTemplate.exchange(
                    MessageFormat.format("{0}bot{1}/getFile?file_id={2}", url, botToken, fileId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return Objects.requireNonNull(response.getBody()).getResult().getFileUrl(this.botToken);
        } catch (Exception e) {
            throw new TelegramFileNotFoundException();
        }
    }
}
