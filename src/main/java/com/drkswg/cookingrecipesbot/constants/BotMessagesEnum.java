package com.drkswg.cookingrecipesbot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public enum BotMessagesEnum {
    HELP_MESSAGE("""
    Привет, я Бот, который поможет тебе воплотить в жизнь твои кулинарные порывы!
    А также я избавлю тебя от необходимости сверлить мужа взглядом, пытаясь узнать у него, что он хочет на ужин!
    
    P.S. Пока я еще ничего не умею, но скоро меня чему-нибудь научат!
    """);

    String message;
}
