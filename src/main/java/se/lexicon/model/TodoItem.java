package se.lexicon.model;

import java.time.LocalDateTime;

public class TodoItem {
    private int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean done;
    private Person creator;

    //Constructors


    public TodoItem(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public TodoItem(String title, String description, LocalDateTime deadline, boolean done, Person creator) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
        this.creator = creator;
    }






    //Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }
}
