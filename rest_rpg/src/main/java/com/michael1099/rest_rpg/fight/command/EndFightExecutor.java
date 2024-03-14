package com.michael1099.rest_rpg.fight.command;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EndFightExecutor {

    private final List<EndFightCommand> endFightCommands = new ArrayList<>();

    public void executeCommand(@NotNull EndFightCommand endFightCommand) {
        endFightCommands.add(endFightCommand);
        endFightCommand.execute();
    }
}
