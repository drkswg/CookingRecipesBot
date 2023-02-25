package com.drkswg.cookingrecipesbot.model;

import com.drkswg.cookingrecipesbot.entity.Recipe;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserStep {
    long chatId;
    String step;
    Recipe recipe;
}
