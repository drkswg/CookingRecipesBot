package com.drkswg.cookingrecipesbot.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "recipes")
@ToString(exclude = "recipes")
public class User {
    @Id
    @Column(name = "user_id")
    long id;

    @Column(name = "name")
    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    Set<Recipe> recipes;
}
