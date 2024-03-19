package com.michael1099.rest_rpg.occupation.state;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.exceptions.CharacterIsNotOnAdventureException;
import com.michael1099.rest_rpg.exceptions.WorkNotFoundException;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.work.model.Work;

import java.time.LocalDateTime;

public class IdleOccupationState implements OccupationState {

    @Override
    public void startAdventure(Adventure adventure, Occupation occupation) {
        occupation.setAdventure(adventure);
        occupation.setFinishTime(LocalDateTime.now().plusMinutes(adventure.getAdventureTimeInMinutes()));
    }

    @Override
    public void endAdventure(Adventure adventure, Occupation occupation) {
        throw new CharacterIsNotOnAdventureException();
    }

    @Override
    public void startWork(Work work, Occupation occupation) {
        occupation.setWork(work);
        occupation.setFinishTime(LocalDateTime.now().plusMinutes(work.getWorkMinutes()));
    }

    @Override
    public void endWork(Work work, Occupation occupation) {
        throw new WorkNotFoundException();
    }
}
