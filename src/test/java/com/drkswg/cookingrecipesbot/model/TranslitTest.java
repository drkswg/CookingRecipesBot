package com.drkswg.cookingrecipesbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslitTest {
    @Test
    void testConvertCyrillicCharacters() {
        String word = "борщ";
        String expected = "borshh";
        String actual = Translit.convert(word);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertWithSpaces() {
        String word = "Рецепт картошки";
        String expected = "recept_kartoshki";
        String actual = Translit.convert(word);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertNonCyrillicCharacters() {
        String word = "Pasta";
        String expected = "pasta";
        String actual = Translit.convert(word);
        assertEquals(expected, actual);
    }

    @Test
    void testConvertMixedCharacters() {
        String word = "Омлет с Bacon";
        String expected = "omlet_s_bacon";
        String actual = Translit.convert(word);
        assertEquals(expected, actual);
    }
}
