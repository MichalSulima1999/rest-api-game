package com.michael1099.rest_rpg.adventure.memento;

import com.michael1099.rest_rpg.enemy.model.Enemy;
import com.michael1099.rest_rpg.occupation.Occupation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

// Tydzień 5, Memento
// Memento pozwala na zachowanie historycznego stanu obiektu.
// Obiekt memento pozwala na zachowanie tego stanu, a caretaker przechowuje historię
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
// Koniec Tydzień 5, Memento
