package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.fight.flyweight.Terrain;

import java.util.Random;

public class TerrainManagerImplementation implements TerrainManager {

    public static int MAP_SIZE = 500;
    public static int ELEMENTS_TO_DRAW = 1000;
    static int OBJECT_TYPES = 3;

    @Override
    public void generateTerrain() {
        Terrain terrain = new Terrain();
        Random random = new Random();
        for (int i = 0; i < (ELEMENTS_TO_DRAW / OBJECT_TYPES); i++) {
            terrain.placeObject(random.nextInt(0, MAP_SIZE),
                    random.nextInt(0, MAP_SIZE), "Oak", "Oak texture", true);
            terrain.placeObject(random.nextInt(0, MAP_SIZE),
                    random.nextInt(0, MAP_SIZE), "Stone", "Stone texture", true);
            terrain.placeObject(random.nextInt(0, MAP_SIZE),
                    random.nextInt(0, MAP_SIZE), "Grass", "Grass texture", false);
        }
        terrain.createTerrain();
    }
}
