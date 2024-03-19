package com.michael1099.rest_rpg.occupation.state;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.work.model.Work;

// Tydzień 6, State
// Stworzony został interfejs, który posiada metody do zaczynania i kończenia pracy i przygody
// Metody implementowane są przez odpowiednie klasy, które w zależności od stanu mają inną logikę.
// Gdy postać pracuje, nie może zacząć przygody, skończyć podróżować, zacząć nowej pracy.
// Analogicznie gdy postać podróżuje.
public interface OccupationState {

    void startAdventure(Adventure adventure, Occupation occupation);

    void endAdventure(Adventure adventure, Occupation occupation);

    void startWork(Work work, Occupation occupation);

    void endWork(Work work, Occupation occupation);
}
// Koniec Tydzień 6, State
