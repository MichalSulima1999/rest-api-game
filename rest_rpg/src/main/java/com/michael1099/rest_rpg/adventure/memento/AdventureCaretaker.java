package com.michael1099.rest_rpg.adventure.memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class AdventureCaretaker {

    final Deque<AdventureMemento> mementos = new ArrayDeque<>();

    public AdventureMemento getMemento() {
        return mementos.pop();
    }

    public void addMemento(AdventureMemento memento) {
        mementos.push(memento);
    }
}
