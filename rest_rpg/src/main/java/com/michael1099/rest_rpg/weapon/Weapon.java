package com.michael1099.rest_rpg.weapon;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class Weapon {

    protected String name;
    protected int damage;

    public void attack() {
        log.info("Attacking with {}, causing {} damage", name, damage);
    }
}
