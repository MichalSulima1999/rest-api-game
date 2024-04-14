package com.michael1099.rest_rpg.minigame;

import java.util.ArrayList;
import java.util.List;

public class Hangman implements HangmanGame {

    private final String wordToGuess;
    private final List<Character> guessedLetters;
    private int attemptsLeft;

    public Hangman(String wordToGuess, int maxAttempts) {
        this.wordToGuess = wordToGuess.toLowerCase();
        this.guessedLetters = new ArrayList<>();
        this.attemptsLeft = maxAttempts;
    }

    @Override
    public void startGame() {
        System.out.println("Welcome to Hangman! Try to guess the word.");
        displayWordStatus();
    }

    @Override
    public void guessLetter(char letter) {
        if (guessedLetters.contains(letter)) {
            System.out.println("You've already guessed that letter!");
            return;
        }

        guessedLetters.add(letter);

        if (wordToGuess.contains(Character.toString(letter))) {
            System.out.println("Correct guess!");
        } else {
            System.out.println("Incorrect guess!");
            attemptsLeft--;
        }

        displayWordStatus();
    }

    private void displayWordStatus() {
        StringBuilder wordStatus = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            if (guessedLetters.contains(c)) {
                wordStatus.append(c);
            } else {
                wordStatus.append("_");
            }
            wordStatus.append(" ");
        }
        System.out.println(wordStatus);
        System.out.println("Attempts left: " + attemptsLeft);

        if (isGameOver()) {
            System.out.println("Game over!");
        }
    }

    private boolean isGameOver() {
        return attemptsLeft <= 0 || wordToGuess.chars().allMatch(c -> guessedLetters.contains((char) c));
    }
}
