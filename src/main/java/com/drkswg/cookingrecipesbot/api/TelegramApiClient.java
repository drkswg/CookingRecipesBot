package com.drkswg.cookingrecipesbot.api;

import exception.TelegramFileNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jvnet.hk2.annotations.Service;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TelegramApiClient {
    @Value("${telegram.api-url}")
    String url;
    @Value("${telegram.bot-token}")
    String botToken;
    RestTemplate restTemplate;

    public File getDocumentFile(String fileId) {
        try {
            return restTemplate.execute(
                    Objects.requireNonNull(getDocumentTelegramFileUrl(fileId)),
                    HttpMethod.GET,
                    null,
                    clientHttpResponse -> {
                        File file = File.createTempFile("download", "tmp");
                        StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(file));
                        return file;
                    });
        } catch (Exception e) {
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
