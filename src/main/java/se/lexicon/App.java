package se.lexicon;

import se.lexicon.controller.TodoController;
import se.lexicon.dao.PeopleDao;
import se.lexicon.dao.TodoItemsDao;
import se.lexicon.dao.impl.PeopleDaoImpl;
import se.lexicon.dao.impl.TodoItemsDaoImpl;
import se.lexicon.view.AppView;
import se.lexicon.view.ConsoleUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todoit", "root", "1234");
            AppView view = new ConsoleUI();
            PeopleDao peopleDao = new PeopleDaoImpl(connection);
            TodoItemsDao todoItemsDao = new TodoItemsDaoImpl(connection);
            TodoController todoController = new TodoController(view, peopleDao, todoItemsDao);
            todoController.run();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}