package com.drkswg.cookingrecipesbot.model;

import java.util.HashMap;
import java.util.Map;

public class MimeDetector {
    public static String getExtension(String mimeType) {
        return dictionary.get(mimeType);
    }

    private static final Map<String, String> dictionary = new HashMap<>() {
        {
            put("image/x-png", ".png");
            put("image/jpeg", ".jpg");
        }
    };
}
