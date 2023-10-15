package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.exceptions.SkillNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    default Skill get(long id) {
        return findById(id).orElseThrow(SkillNotFoundException::new);
    }
}
