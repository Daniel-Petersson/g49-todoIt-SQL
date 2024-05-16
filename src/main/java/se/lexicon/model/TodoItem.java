package se.lexicon.model;

import java.time.LocalDate;


public class TodoItem {
    private int id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean done;
    private Person creator;

    //Constructors


    public TodoItem(String title, String description, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public TodoItem(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public TodoItem(String title, String description, LocalDate deadline, boolean done, Person creator) {
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

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
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

    public String todoInfo(){
        return "TodoItem id: "+getId()+
                "\n Title: " +getTitle()+
                "\n Title: " +getDeadline()+
                "\n Title: " +getDescription()+
                "\n Title: " +getCreator();

    }

    @Override
public String toString() {
    return "TodoItem{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", deadline=" + deadline +
            ", done=" + done +
            ", creator=" + getCreator() +
            '}';
}

}

