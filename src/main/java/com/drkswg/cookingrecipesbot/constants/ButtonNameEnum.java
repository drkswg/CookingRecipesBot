package com.drkswg.cookingrecipesbot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public enum ButtonNameEnum {
    BREAKFASTS("Завтраки"),
    SOUPS("Супы"),
    DISHES("Горячие блюда");


    String name;
}
