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

public class TodoController {
    private AppView view;
    private PeopleDao peopleDao;
    private TodoItemsDao todoItemsDao;

    public TodoController(AppView view, PeopleDao peopleDao, TodoItemsDao todoItemsDao) {
        this.view = view;
        this.peopleDao = peopleDao;
        this.todoItemsDao = todoItemsDao;
    }

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
                    markTaskAsDone();
                    break;
                case 8:
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
        displayAllUsers();
        System.out.println("Please enter the ID of the person who will be the assignee of the TodoItem:");
        int assigneeId = Integer.parseInt(view.promoteString());
        Person assignee = peopleDao.findById(assigneeId).orElseThrow(() -> new RuntimeException("Assignee not found in database"));
        TodoItem newTodoItem = view.promoteTodoItemForm();
        newTodoItem.setAssignee(assignee);
        todoItemsDao.create(newTodoItem);
    }

    private void viewAllTodoItems() {
        displayAllUsers();
        view.displayMessage("Enter the id of the person todo items you want to see");
        int personId = Integer.parseInt(view.promoteString());
        Person chosenPerson = peopleDao.findById(personId).orElseThrow(() -> new RuntimeException("Person not found in database"));
        Collection<TodoItem> tasks = todoItemsDao.findAllByAssignee(chosenPerson);
        for (TodoItem task : tasks) {
            System.out.println(task);
        }
    }

    // In TodoController.java
    private void deleteUser() {
        displayAllUsers();
        view.displayMessage("Enter the id of the person you want to delete");
        int personId = Integer.parseInt(view.promoteString());
        Person personToDelete = peopleDao.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        todoItemsDao.deleteByAssignee(personToDelete);
        peopleDao.deleteById(personId);
    }

    private void deleteTask() {
        view.displayMessage("Enter the id of the task you want to delete");
        int taskId = Integer.parseInt(view.promoteString());
        todoItemsDao.deleteById(taskId);
    }

    private void updateUser() {
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

    private void updateTask() {
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

  private void markTaskAsDone() {
    displayAllUsers();
    System.out.println("Enter the id of the person whose task you want to view");
    int personId = Integer.parseInt(view.promoteString());
    Person chosenPerson = peopleDao.findById(personId).orElseThrow(() -> new RuntimeException("Person not found in database"));
    Collection<TodoItem> tasks = todoItemsDao.findAllByAssignee(chosenPerson);
    for (TodoItem task : tasks) {
        System.out.println(task);
    }
    System.out.println("Please enter the ID of the task you want to mark as done:");
    int taskId = Integer.parseInt(view.promoteString());
    TodoItem chosenTask = todoItemsDao.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found in database"));

    if (view.promoteDoneStatus()) {
        chosenTask.setDone(true);
        todoItemsDao.update(chosenTask);
        tasks = todoItemsDao.findAllByAssignee(chosenPerson); // Update the list of tasks
    } else {
        System.out.println("Task was not marked as done.");
    }
}

    private void displayAllUsers() {
        List<Person> people = peopleDao.findAll();
        for (Person person : people) {
            System.out.println(person.personInfo());
        }
    }

}