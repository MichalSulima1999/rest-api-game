package com.michael1099.rest_rpg.weapon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeaponService {

    private final Weapon weapon;

    void attack() {
        weapon.attack();
    }
}
