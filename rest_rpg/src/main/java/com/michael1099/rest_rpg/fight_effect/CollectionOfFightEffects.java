package com.michael1099.rest_rpg.fight_effect;

import com.michael1099.rest_rpg.helpers.iterator.Container;
import com.michael1099.rest_rpg.helpers.iterator.Iterator;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class CollectionOfFightEffects implements Container {

    private Collection<FightEffect> fightEffects;

    @Override
    public Iterator getIterator() {
        return new CollectionOfFightEffectsIterator();
    }

    private class CollectionOfFightEffectsIterator implements Iterator {

        int i;

        @Override
        public boolean hasNext() {
            return i < fightEffects.size();
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                return fightEffects.toArray()[i++];
            }
            return null;
        }
    }
}
