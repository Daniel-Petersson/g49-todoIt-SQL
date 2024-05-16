package se.lexicon.view;

import se.lexicon.model.Person;
import se.lexicon.model.TodoItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleUI implements AppView {
    private Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayMenu() {
        System.out.println("0. Create User");
        System.out.println("1. Create TodoItem");
        System.out.println("2. View All TodoItems");
        System.out.println("3. Delete User");
        System.out.println("4. Delete Task");
        System.out.println("5. Update User");
        System.out.println("6. Update Task");
        System.out.println("7. Mark Task as done");
        System.out.println("8. Exit");
        System.out.print("Enter choice: ");
    }

    @Override
    public void displayPerson(Person person) {
        System.out.println(person.personInfo());
        System.out.println("--------------------");
    }

    @Override
    public void displayTodoItem(TodoItem todoItem) {
        System.out.println(todoItem.todoInfo());
        System.out.println("--------------------");
    }


    @Override
    public Person promotePersonForm() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first name");
        String firstname = scanner.nextLine();
        System.out.println("Enter last name");
        String lastname = scanner.nextLine();
        return new Person(firstname, lastname);
    }

    @Override
    public TodoItem promoteTodoItemForm() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter title");
        String title = scanner.nextLine();
        System.out.println("Enter description");
        String description = scanner.nextLine();
        System.out.println("Enter deadline (yyyy-MM-dd)");
        String date = scanner.nextLine();

        LocalDate deadline = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new TodoItem(title,description,deadline);
    }

    @Override
    public String promoteString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public boolean promoteDoneStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to mark the task as done? (yes/no)");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("yes");
    }
}
