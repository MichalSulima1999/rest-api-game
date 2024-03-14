package com.michael1099.rest_rpg.fight.command;

// Tydzień 5, Command
// Stworzony został interfejs, po którym dziedziczą LoseFightCommand i WinFightCommand
// Klasy command to LoseFightCommand i WinFightCommand, klasą receiver jest EndFight - zawiera logikę
// EndFightExecutor to klasa invoker, która przechowuje historię komend i je wykonuje
public interface EndFightCommand {

    void execute();
}
// Koniec Tydzień 5, Command
