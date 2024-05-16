package se.lexicon.controller;

import se.lexicon.dao.PeopleDao;
import se.lexicon.dao.TodoItemsDao;
import se.lexicon.model.Person;
import se.lexicon.view.AppView;
import se.lexicon.model.TodoItem;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TodoController {
    private AppView view;
    private PeopleDao peopleDao;
    private TodoItemsDao todoItemsDao;

    public TodoController(AppView view, PeopleDao peopleDao, TodoItemsDao todoItemsDao) {
        this.view = view;
        this.peopleDao = peopleDao;
        this.todoItemsDao = todoItemsDao;
    }

    //Todo add assignnee to task. Set done deafault to false. create method to mark task done. Display all users, and the view there task.
    public void run() {
    boolean running = true;
    while (running) {
        view.displayMenu();
        int choice = getUserChoice();
        switch (choice) {
            case 0:
                createUser();
                break;
            case 1:
                createTodoItem();
                break;
            case 2:
                viewAllTodoItems();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                deleteTask();
                break;
            case 5:
                updateUser();
                break;
            case 6:
                updateTask();
                break;
            case 7:
                running = false;
                break;
            default:
                view.displayMessage("Invalid choice. Please select a valid option.");
        }
    }
}

    private int getUserChoice() {
        String operationType = view.promoteString();
        int choice = -1;
        try {
            choice = Integer.parseInt(operationType);
        } catch (NumberFormatException e) {
            view.displayMessage("Invalid input. Please enter a number");
        }
        return choice;
    }

   private void createUser() {
    Person newPerson = view.promotePersonForm();
    peopleDao.create(newPerson);
}

 private void createTodoItem() {
    // Show all people
    List<Person> people = peopleDao.findAll();
    for (Person person : people) {
        System.out.println(person.personInfo());
    }

    // Ask the user to choose a person
    System.out.println("Please enter the ID of the person who will be the creator of the TodoItem:");
    int creatorId = Integer.parseInt(view.promoteString());

    // Get the chosen person from the database
    Person creator = peopleDao.findById(creatorId).orElseThrow(() -> new RuntimeException("Creator not found in database"));

    // Promote for TodoItem details
    TodoItem newTodoItem = view.promoteTodoItemForm();

    // Set the chosen person as the creator of the TodoItem
    newTodoItem.setCreator(creator);

    // Save the TodoItem to the database
    todoItemsDao.create(newTodoItem);
}

    private void viewAllTodoItems() {
        System.out.println("All available todo items");
        Collection<TodoItem> todoItems = todoItemsDao.findAll();
        for (TodoItem item : todoItems) System.out.println(item);
    }

    private void deleteUser() {
        view.displayMessage("Enter the id of the person you want to delete");
        int personId = Integer.parseInt(view.promoteString());
        peopleDao.deleteById(personId);
    }

    private void deleteTask() {
        view.displayMessage("Enter the id of the task you want to delete");
        int taskId = Integer.parseInt(view.promoteString());
        todoItemsDao.deleteById(taskId);
    }

    private void updateUser(){
        view.displayMessage("Enter the id of the person you want to update");
        int personId = Integer.parseInt(view.promoteString());
        view.displayMessage("Enter new firstname");
        String newFirstname = view.promoteString();
        view.displayMessage("Enter new lastname");
        String newLastname = view.promoteString();
        Person personToUpdate = peopleDao.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        personToUpdate.setFirstName(newFirstname);
        personToUpdate.setLastName(newLastname);
        peopleDao.update(personToUpdate);
    }

    private void updateTask(){
        view.displayMessage("Enter the id of the task you want to update");
        int taskId = Integer.parseInt(view.promoteString());
        view.displayMessage("Enter new title");
        String newTitle = view.promoteString();
        view.displayMessage("Enter new description");
        String newDescription = view.promoteString();
        view.displayMessage("Enter new deadline (yyyy-MM-dd)");
        String newDeadlineString = view.promoteString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDeadline = LocalDate.parse(newDeadlineString, formatter);
        TodoItem taskToUpdate = todoItemsDao.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        taskToUpdate.setTitle(newTitle);
        taskToUpdate.setDescription(newDescription);
        taskToUpdate.setDeadline(newDeadline);
        todoItemsDao.update(taskToUpdate);
    }
}