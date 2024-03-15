package com.michael1099.rest_rpg.adventure.memento;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.occupation.Occupation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class AdventureMemento {

    private Long id;

    private String name;

    private int adventureTimeInMinutes;

    private int xpForAdventure;

    private int goldForAdventure;

    private Enemy enemy;

    private Set<Occupation> occupation;

    private boolean deleted;
}
