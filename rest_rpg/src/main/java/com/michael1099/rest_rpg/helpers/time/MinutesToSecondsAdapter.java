package com.michael1099.rest_rpg.helpers.time;

import lombok.AllArgsConstructor;

// Tydzień 3, Adapter
// Implementacja adaptera - pobierany jest ObjectWithTime, który posiada czas w minutach, który jest konwertowany na czas w sekundach
@AllArgsConstructor
public class MinutesToSecondsAdapter implements TimeAdapter {

    private ObjectWithTime objectWithTime;

    @Override
    public int convert() {
        return objectWithTime.getTimeInMinutes() * 60;
    }
}
// Koniec Tydzień 3, Adapter
