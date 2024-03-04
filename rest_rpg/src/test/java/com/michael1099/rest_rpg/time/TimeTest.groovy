package com.michael1099.rest_rpg.time

import com.michael1099.rest_rpg.adventure.model.Adventure
import com.michael1099.rest_rpg.helpers.time.MinutesToFullHoursAdapter
import com.michael1099.rest_rpg.helpers.time.MinutesToSecondsAdapter
import spock.lang.Specification

class TimeTest extends Specification {

    // Tydzień 3, Adapter
    // Test adaptera
    def "should convert time"() {
        given:
            def adventure = Adventure.builder().adventureTimeInMinutes(10).build()
        when:
            def adapter = new MinutesToSecondsAdapter(adventure)
            def timeInSeconds = adapter.convert()
        then:
            adventure.timeInMinutes == 10
            timeInSeconds == 600
        when:
            adventure.adventureTimeInMinutes = 70
            adapter = new MinutesToFullHoursAdapter(adventure)
            def timeInHours = adapter.convert()
        then:
            adventure.timeInMinutes == 70
            timeInHours == 1
    }
    // Koniec Tydzień 3, Adapter
}
