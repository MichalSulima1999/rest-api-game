package com.michael1099.rest_rpg.helpers.time;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinutesToFullHoursAdapter implements TimeAdapter {

    private ObjectWithTime objectWithTime;

    @Override
    public int convert() {
        return objectWithTime.getTimeInMinutes() / 60;
    }
}
