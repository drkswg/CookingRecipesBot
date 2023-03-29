package com.drkswg.cookingrecipesbot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recipes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "recipeCategory")
@ToString(exclude = {"recipeCategory", "recipeSteps"})
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rc_fk", nullable = false)
    RecipeCategory recipeCategory;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe")
    Set<RecipeStep> recipeSteps;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe")
    Set<Photo> photos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    User user;
}
