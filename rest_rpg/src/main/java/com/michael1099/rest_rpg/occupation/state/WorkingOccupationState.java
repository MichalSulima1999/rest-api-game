package com.michael1099.rest_rpg.occupation.state;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.exceptions.CharacterIsAtWorkException;
import com.michael1099.rest_rpg.exceptions.EnumValueNotFoundException;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.work.EndWork;
import com.michael1099.rest_rpg.work.EndWorkWithGold;
import com.michael1099.rest_rpg.work.EndWorkWithIron;
import com.michael1099.rest_rpg.work.EndWorkWithWood;
import com.michael1099.rest_rpg.work.model.Work;

public class WorkingOccupationState implements OccupationState {

    @Override
    public void startAdventure(Adventure adventure, Occupation occupation) {
        throw new CharacterIsAtWorkException();
    }

    @Override
    public void endAdventure(Adventure adventure, Occupation occupation) {
        throw new CharacterIsAtWorkException();
    }

    @Override
    public void startWork(Work work, Occupation occupation) {
        throw new CharacterIsAtWorkException();
    }

    @Override
    public void endWork(Work work, Occupation occupation) {
        // Tydzień 6, Template
        // Stworzona została abstrakcyjna klasa EndWork, która rozszerza EndWorkWithGold, EndWorkWithIron i EndWorkWithWood
        // Metoda getResources() jest nadpisywana w tych klasach
        EndWork endWork;
        switch (work.getResourceType()) {
            case GOLD -> endWork = new EndWorkWithGold(occupation, occupation.getCharacter().getEquipment(), work);
            case IRON -> endWork = new EndWorkWithIron(occupation, occupation.getCharacter().getEquipment(), work);
            case WOOD -> endWork = new EndWorkWithWood(occupation, occupation.getCharacter().getEquipment(), work);
            default -> throw new EnumValueNotFoundException();
        }
        // Koniec Tydzień 6, Template
        endWork.finishWork();
    }
}
