package com.michael1099.rest_rpg.task;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskService {

    private final Task task;

    void startTask() {
        task.startTask();
    }

    void endTask() {
        task.endTask();
    }
}
