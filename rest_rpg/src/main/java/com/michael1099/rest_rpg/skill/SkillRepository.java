package com.michael1099.rest_rpg.skill;

import com.michael1099.rest_rpg.exceptions.SkillNotFoundException;
import com.michael1099.rest_rpg.skill.model.Skill;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, SkillRepositoryCustom {

    boolean existsByNameIgnoreCase(@NotBlank String name);

    default Skill get(long id) {
        return findById(id).orElseThrow(SkillNotFoundException::new);
    }
}
