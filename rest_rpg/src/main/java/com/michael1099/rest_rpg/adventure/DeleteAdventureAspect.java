package com.michael1099.rest_rpg.adventure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

// Tydzień 11 - użyj w programie AspectJ
// Klasa DeleteAdventureAspect jest oznaczona adnotacją @Aspect z org.aspectj
// Adnotacje @Before i @After (pointcut) zawierają ścieżkę do metody, po której (lub przed którą) ma się wykonać metoda pod adnotacją
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteAdventureAspect {

    private final AdventureRepository adventureRepository;

    @Before("execution(* com.michael1099.rest_rpg.adventure.AdventureService.deleteAdventure(..)) && args(adventureId)")
    public void beforeDeleteAdventure(long adventureId) {
        var adventure = adventureRepository.findById(adventureId).orElseThrow();
        if (adventure.isDeleted()) {
            log.info("Trying to delete adventure with id {} that is already deleted", adventureId);
        }
    }

    @After("execution(* com.michael1099.rest_rpg.adventure.AdventureService.deleteAdventure(..)) && args(adventureId)")
    public void afterDeleteAdventure(long adventureId) {
        log.info("Deleted adventure with id {}", adventureId);
    }
}
// Koniec Tydzień 11 - użyj w programie AspectJ
