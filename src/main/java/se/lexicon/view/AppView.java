package se.lexicon.view;

import se.lexicon.model.Person;
import se.lexicon.model.TodoItem;

public interface AppView {

    void displayMenu();

    void displayPerson(Person person);

    void displayTodoItem(TodoItem todoItem);

    default void displayMessage(String message) {
        System.out.println(message);
    }

    String promoteString();

    Person promotePersonForm();

    TodoItem promoteTodoItemForm();


}
