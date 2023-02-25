package com.drkswg.cookingrecipesbot.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public enum ButtonNameEnum {
    BREAKFASTS("Завтраки", "recipeCategory"),
    SOUPS("Супы", "recipeCategory"),
    DISHES("Горячие блюда", "recipeCategory"),
    ADD_RECIPE("Добавить рецепт", "service");

    String name;
    String type;
}
