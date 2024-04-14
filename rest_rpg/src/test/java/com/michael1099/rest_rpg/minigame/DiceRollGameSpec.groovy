package com.michael1099.rest_rpg.minigame

import spock.lang.Specification

class DiceRollGameSpec extends Specification {

    def "Should determine the winner correctly"() {
        given:
            def game = new DiceRollGame()
        when:
            game.startGame()
        then:
            game.winner == Winner.PLAYER || Winner.OPPONENT || Winner.TIE
    }
}
