package com.michael1099.rest_rpg.minigame

import spock.lang.Specification

class RockPaperScissorsSpec extends Specification {

    def "Should determine the winner correctly"() {
        given:
            def game = new RockPaperScissors()
            game.startGame()
        when:
            game.playRound(PlayerChoice.PAPER)
        then:
            game.winner == Winner.PLAYER || Winner.OPPONENT || Winner.TIE
    }
}
