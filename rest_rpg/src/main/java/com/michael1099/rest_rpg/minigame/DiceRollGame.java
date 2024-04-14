package com.michael1099.rest_rpg.minigame;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DiceRollGame implements DiceGame {

    private Winner winner;

    @Override
    public void startGame() {
        System.out.println("Let's roll the dice!");
        rollDice();
    }

    @Override
    public void rollDice() {
        int opponentDiceResult = (int) (Math.random() * 6) + 1;
        int diceResult = (int) (Math.random() * 6) + 1;
        System.out.println("Player rolled: " + diceResult);
        System.out.println("Opponent rolled: " + opponentDiceResult);

        if (diceResult > opponentDiceResult) {
            System.out.println("Player wins!");
            winner = Winner.PLAYER;
        } else if (diceResult < opponentDiceResult) {
            System.out.println("Opponent wins!");
            winner = Winner.OPPONENT;
        } else {
            System.out.println("It's a tie!");
            winner = Winner.TIE;
        }
    }
}
