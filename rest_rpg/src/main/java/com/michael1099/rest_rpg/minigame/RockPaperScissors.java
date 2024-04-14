package com.michael1099.rest_rpg.minigame;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RockPaperScissors implements RockPaperScissorsGame {

    private Winner winner;

    @Override
    public void startGame() {
        System.out.println("Let's play Rock, Paper, Scissors!");
        System.out.println("Enter your choice (rock, paper, or scissors)");
    }

    @Override
    public void playRound(PlayerChoice playerChoice) {
        PlayerChoice[] choices = PlayerChoice.values();
        PlayerChoice computerChoice = choices[(int) (Math.random() * choices.length)];

        System.out.println("Player chose: " + playerChoice);
        System.out.println("Computer chose: " + computerChoice);

        if (playerChoice == computerChoice) {
            System.out.println("It's a tie!");
            winner = Winner.TIE;
        } else if ((playerChoice == PlayerChoice.ROCK && computerChoice == PlayerChoice.SCISSORS) ||
                (playerChoice == PlayerChoice.SCISSORS && computerChoice == PlayerChoice.PAPER) ||
                (playerChoice == PlayerChoice.PAPER && computerChoice == PlayerChoice.ROCK)) {
            System.out.println("Player wins!");
            winner = Winner.PLAYER;
        } else {
            System.out.println("Computer wins!");
            winner = Winner.OPPONENT;
        }
    }
}
