package se.lexicon.view;

public interface AppView {

    void displayMenu();

    int getMenuChoice();

    String promoteString(String prompt);

    int promotePerson(String prompt);

    int promoteTodoItem(String prompt);

    void displayMessage(String message);

}
