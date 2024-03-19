package com.michael1099.rest_rpg.occupation.state;

import com.michael1099.rest_rpg.adventure.model.Adventure;
import com.michael1099.rest_rpg.exceptions.FightIsOngoingException;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.work.model.Work;

public class FightOccupationState implements OccupationState {

    @Override
    public void startAdventure(Adventure adventure, Occupation occupation) {
        throw new FightIsOngoingException();
    }

    @Override
    public void endAdventure(Adventure adventure, Occupation occupation) {
        throw new FightIsOngoingException();
    }

    @Override
    public void startWork(Work work, Occupation occupation) {
        throw new FightIsOngoingException();
    }

    @Override
    public void endWork(Work work, Occupation occupation) {
        throw new FightIsOngoingException();
    }
}
