package com.michael1099.rest_rpg.weapon;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Bow extends Weapon {

    private final int range;

    public Bow(String name, int damage, int range) {
        super(name, damage);
        this.range = range;
    }

    @Override
    public void attack() {
        log.info("Firing arrows with {}, causing {} damage at range {}", name, damage, range);
    }
}
