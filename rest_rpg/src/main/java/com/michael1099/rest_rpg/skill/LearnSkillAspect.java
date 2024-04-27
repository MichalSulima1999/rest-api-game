package com.michael1099.rest_rpg.skill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LearnSkillAspect {

    private final SkillRepository skillRepository;

    @Before("execution(* com.michael1099.rest_rpg.skill.SkillService.learnSkill(..)) && args(skillId, characterId)")
    public void beforeLearnSkill(long skillId, long characterId) {
        var skill = skillRepository.findById(skillId).orElseThrow();
        log.info("Character with id {} is trying to learn skill {}.", characterId, skill.getName());
    }
}
