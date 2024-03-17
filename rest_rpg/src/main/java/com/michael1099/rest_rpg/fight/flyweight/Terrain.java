package com.michael1099.rest_rpg.fight.flyweight;

import java.util.ArrayList;
import java.util.List;

// Tydzień 4 - Flyweight
// Tworząc teren wykorzystujemy wiele różnych obiektów typu kamień, drzewo, trawa itp.
// Dane drzewo może różnić się od innego tego samego drzewa wyłącznie lokacją, więc możemy przechować dane drzewo
// w liście, a następnie je pobrać jeżeli chcemy ustawić takie samo drzewo w innym miejscu
public class Terrain {

    private final List<MapObject> mapObjects = new ArrayList<>();

    public void placeObject(int x, int y, String name, String texture, boolean isSolid) {
        ObjectType type = MapObjectFactory.getObjectType(name, texture, isSolid);
        MapObject mapObject = new MapObject(x, y, type);
        mapObjects.add(mapObject);
    }

    public void createTerrain() {
        for (MapObject mapObject : mapObjects) {
            mapObject.draw();
        }
    }
}
// Koniec Tydzień 4 - Flyweight
