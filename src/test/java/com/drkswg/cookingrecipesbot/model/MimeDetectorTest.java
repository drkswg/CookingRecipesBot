package com.drkswg.cookingrecipesbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MimeDetectorTest {
    @Test
    void testGetExtensionForPNG() {
        String mimeType = "image/x-png";
        String expectedExtension = ".png";
        String actualExtension = MimeDetector.getExtension(mimeType);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void testGetExtensionForJPEG() {
        String mimeType = "image/jpeg";
        String expectedExtension = ".jpg";
        String actualExtension = MimeDetector.getExtension(mimeType);
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void testGetExtensionForUnknownMimeType() {
        String mimeType = "application/unknown";
        String actualExtension = MimeDetector.getExtension(mimeType);
        assertNull(actualExtension);
    }

    @Test
    void testGetExtensionForNullInput() {
        String actualExtension = MimeDetector.getExtension(null);
        assertNull(actualExtension);
    }
}
