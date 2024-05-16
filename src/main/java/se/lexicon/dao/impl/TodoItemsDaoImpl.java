package se.lexicon.dao.impl;

import se.lexicon.Exception.MySQLException;
import se.lexicon.dao.TodoItemsDao;
import se.lexicon.model.TodoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TodoItemsDaoImpl implements TodoItemsDao {
    private final Connection connection;

    public TodoItemsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
public Optional<TodoItem> create(TodoItem model) {
    String query = "INSERT INTO todo_item (title, description) VALUES (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, model.getTitle());
        preparedStatement.setString(2, model.getDescription());
        int numberOfRowsInserted = preparedStatement.executeUpdate();
        if (numberOfRowsInserted > 0) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getInt(1));
                    return Optional.of(model);
                }
            }
        }
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while creating todo item.", e);
    }
    return Optional.empty();
}

@Override
public Optional<TodoItem> findById(Integer id) {
    String query = "SELECT * FROM todo_item WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, id);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(getTodoItem(resultSet));
            }
        }
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while finding todo item by id.", e);
    }
    return Optional.empty();
}

@Override
public Collection<TodoItem> find(String title) {
    String query = "SELECT * FROM todo_item WHERE title LIKE ?";
    List<TodoItem> todoItems = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, "%" + title + "%");
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                todoItems.add(getTodoItem(resultSet));
            }
        }
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while finding todo items by title.", e);
    }
    return todoItems;
}

@Override
public List<TodoItem> findAll() {
    String query = "SELECT * FROM todo_item";
    List<TodoItem> todoItems = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
            todoItems.add(getTodoItem(resultSet));
        }
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while finding all todo items.", e);
    }
    return todoItems;
}

@Override
public boolean deleteById(Integer id) {
    String query = "DELETE FROM todo_item WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, id);
        int rowsDeleted = preparedStatement.executeUpdate();
        return rowsDeleted > 0;
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while deleting todo item by id.", e);
    }
}

@Override
public TodoItem update(TodoItem model) {
    String query = "UPDATE todo_item SET title = ?, description = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, model.getTitle());
        preparedStatement.setString(2, model.getDescription());
        preparedStatement.setInt(3, model.getId());
        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            return model;
        }
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while updating todo item.", e);
    }
    return null;
}

private TodoItem getTodoItem(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id");
    String title = resultSet.getString("title");
    String description = resultSet.getString("description");
    return new TodoItem(id, title, description);
}
}
