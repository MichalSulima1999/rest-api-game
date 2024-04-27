package com.michael1099.rest_rpg.fight;

import com.michael1099.rest_rpg.fight.flyweight.Terrain;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class TerrainManagerImplementation implements TerrainManager {

    public static int MAP_SIZE = 500;
    public static int ELEMENTS_TO_DRAW = 1000;
    static int OBJECT_TYPES = 3;
    private Terrain terrain;

    @Override
    public void generateTerrain() {
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
