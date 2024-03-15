package com.michael1099.rest_rpg.fight.flyweight;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapObject {

    private int x;
    private int y;
    private ObjectType objectType;

    public void draw() {
        objectType.draw(x, y);
    }
}
