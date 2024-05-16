package se.lexicon.model;

public class TodoItemTask {
    private int id;
    private boolean assigned;
    private TodoItem todoitem;
    private Person assignee;

    //Constructors

    //Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public TodoItem getTodoitem() {
        return todoitem;
    }

    public void setTodoitem(TodoItem todoitem) {
        this.todoitem = todoitem;
    }

    public Person getAssignee() {
        return assignee;
    }

    public void setAssignee(Person assignee) {
        this.assignee = assignee;
    }
}
