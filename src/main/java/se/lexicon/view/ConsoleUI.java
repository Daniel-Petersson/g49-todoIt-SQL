package se.lexicon.view;

import java.util.Scanner;

public class ConsoleUI implements AppView {
    private Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Create User");
        System.out.println("2. Create TodoItem");
        System.out.println("3. View All TodoItems");
        System.out.println("4. Delete User");
        System.out.println("5. Delete Task");
        System.out.println("6. Update Task");
        System.out.println("7. Update User");
        System.out.println("8. Exit");
        System.out.print("Enter choice: ");
    }

    @Override
    public int getMenuChoice() {
        System.out.print("Enter your choice: ");
        return Integer.parseInt(getInput());
    }

    @Override
    public String promoteString(String prompt) {
        System.out.print(prompt);
        return getInput();
    }

    @Override
    public int promotePerson(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(getInput());
    }

    @Override
    public int promoteTodoItem(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(getInput());
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    private String getInput() {
        return scanner.nextLine();
    }
}
