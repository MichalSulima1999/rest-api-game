package com.michael1099.rest_rpg.work;

import com.michael1099.rest_rpg.occupation.Occupation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class EndWork {

    protected Occupation occupation;

    public final void finishWork() {
        getResources();
        occupation.setWork(null);
    }

    public abstract void getResources();
}
