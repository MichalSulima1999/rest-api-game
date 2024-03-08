package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.skill.model.Skill;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SkillCache {

    private final Map<Long, Skill> cache = new HashMap<>();

    public Skill getEntity(Long id) {
        return cache.get(id);
    }

    public void putEntity(Long id, Skill entity) {
        cache.put(id, entity);
    }

    public boolean containsEntity(Long id) {
        return cache.containsKey(id);
    }
}
