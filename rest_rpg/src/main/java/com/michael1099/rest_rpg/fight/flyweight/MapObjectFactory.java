package com.michael1099.rest_rpg.fight.flyweight;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MapObjectFactory {

    static Map<String, ObjectType> mapObjectTypes = new HashMap<>();

    public static ObjectType getObjectType(String name, String texture, boolean isSolid) {
        ObjectType result = mapObjectTypes.get(name);
        if (result == null) {
            result = new ObjectType(name, texture, isSolid);
            mapObjectTypes.put(name, result);
        }
        return result;
    }
}
