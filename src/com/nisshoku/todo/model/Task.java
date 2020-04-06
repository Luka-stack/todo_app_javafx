package com.nisshoku.todo.model;

import java.time.LocalDateTime;

public class Task {

    private LocalDateTime dateCreated;
    private String description;
    private String task;

    public Task() { }

    public Task(String task, String description) {
        this.description = description;
        this.task = task;
    }

    public LocalDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }
}
