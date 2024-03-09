package com.michael1099.rest_rpg.work;

import com.michael1099.rest_rpg.equipment.Equipment;
import com.michael1099.rest_rpg.occupation.Occupation;
import com.michael1099.rest_rpg.work.model.Work;

public class EndWorkWithIron extends EndWork {

    private final Equipment equipment;
    private final Work work;

    public EndWorkWithIron(Occupation occupation, Equipment equipment, Work work) {
        super(occupation);
        this.equipment = equipment;
        this.work = work;
    }

    @Override
    public void getResources() {
        equipment.addIron(work.getResourceAmount());
    }
}
