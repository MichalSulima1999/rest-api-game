package com.michael1099.rest_rpg.fight.flyweight;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ObjectType {

    private String name;
    private String texture;
    private boolean isSolid;

    public void draw(int x, int y) {
        log.info("Drawing a {} with texture {} at x: {} y: {}. Solid object: {}", name, texture, x, y, isSolid);
    }
}
