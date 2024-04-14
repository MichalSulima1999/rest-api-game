package com.michael1099.rest_rpg.minigame

import spock.lang.Specification

class HangmanSpec extends Specification {

    def hangman

    def setup() {
        hangman = new Hangman("hangman", 5)
    }

    def "Should correctly guess letters and end the game"() {
        given:
            hangman.startGame()
        when:
            hangman.guessLetter('h' as char)
            hangman.guessLetter('a' as char)
            hangman.guessLetter('n' as char)
            hangman.guessLetter('g' as char)
            hangman.guessLetter('m' as char)
        then:
            hangman.attemptsLeft == 5
        when:
            hangman.guessLetter('z' as char)
        then:
            hangman.attemptsLeft == 4
    }
}
