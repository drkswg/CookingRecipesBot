package com.drkswg.cookingrecipesbot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "recipe_steps")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "recipe")
@ToString(exclude = "recipe")
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rs_id")
    int id;

    @Column(name = "step")
    int step;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_fk", nullable = false)
    Recipe recipe;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipeStep")
    Set<Photo> photos;
}
