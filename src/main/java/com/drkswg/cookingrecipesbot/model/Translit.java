package com.drkswg.cookingrecipesbot.model;

import java.util.HashMap;
import java.util.Map;

public class Translit {
    public static String convert(String word) {
        StringBuilder translited = new StringBuilder();

        for (char letter : word.toCharArray()) {
            if (letter == ' ') {
                translited.append("_");
            } else {
                char tempLetter = Character.isUpperCase(letter)
                        ? Character.toLowerCase(letter)
                        : letter;
                String transChar = dictionary.get(tempLetter) == null ?
                        String.valueOf(tempLetter)
                        : dictionary.get(tempLetter);
                translited.append(transChar);
            }
        }

        return translited.toString();
    }

    private static final Map<Character, String> dictionary = new HashMap<>() {
        {
            put('а', "a");
            put('б', "b");
            put('в', "v");
            put('г', "g");
            put('д', "d");
            put('е', "e");
            put('ж', "zh");
            put('з', "z");
            put('и', "i");
            put('й', "j");
            put('к', "k");
            put('л', "l");
            put('м', "m");
            put('н', "n");
            put('о', "o");
            put('п', "p");
            put('р', "r");
            put('с', "s");
            put('т', "t");
            put('у', "u");
            put('ф', "f");
            put('х', "x");
            put('ц', "c");
            put('ч', "ch");
            put('ш', "sh");
            put('щ', "shh");
            put('ы', "y");
            put('э', "e");
            put('ю', "yu");
            put('я', "ya");
        }
    };
}
