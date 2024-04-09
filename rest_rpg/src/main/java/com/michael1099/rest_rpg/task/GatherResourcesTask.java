package com.michael1099.rest_rpg.task;

import com.michael1099.rest_rpg.character.model.Character;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ResourceType;

@Slf4j
@RequiredArgsConstructor
public class GatherResourcesTask implements Task {

    private final Character character;
    private final ResourceType resourceType;
    private final int amount;

    @Override
    public void startTask() {
        log.info("Starting quest for gathering {} of {}", resourceType, amount);
        character.acceptResourcesTask(resourceType, amount);
    }

    @Override
    public void endTask() {
        log.info("Ending quest for gathering {} of {}", resourceType, amount);
        character.endResourcesTask();
    }
}
