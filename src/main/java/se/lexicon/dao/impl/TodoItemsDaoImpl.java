package se.lexicon.dao.impl;

import se.lexicon.Exception.MySQLException;
import se.lexicon.dao.PeopleDao;
import se.lexicon.dao.TodoItemsDao;
import se.lexicon.model.Person;
import se.lexicon.model.TodoItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TodoItemsDaoImpl implements TodoItemsDao {
    private final Connection connection;
    private final PeopleDao peopleDao;

    public TodoItemsDaoImpl(Connection connection, PeopleDao peopleDao) {
        this.connection = connection;
        this.peopleDao = peopleDao;
    }

@Override
public Optional<TodoItem> create(TodoItem model) {
    if (model.getAssignee() == null) {
        throw new IllegalArgumentException("Creator cannot be null");
    }
    String query = "INSERT INTO todo_item (title, description, deadline, done, assignee_id) VALUES (?,?,?,?,?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, model.getTitle());
        preparedStatement.setString(2, model.getDescription());
        java.sql.Date sqlDate = java.sql.Date.valueOf(model.getDeadline());
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(sqlDate.getTime());
        preparedStatement.setTimestamp(3, sqlTimestamp);
        preparedStatement.setBoolean(4, model.isDone());
        preparedStatement.setInt(5,model.getAssignee().getId());
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
    String query = "SELECT * FROM todo_item WHERE todo_id = ?";
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
    public Collection<TodoItem> findAllByAssignee(Person person) {
        String query = "SELECT* FROM todo_item WHERE assignee_id = ?";
        List<TodoItem>  todoItems = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setInt(1,person.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    todoItems.add(getTodoItem(resultSet));
                }
            }
        }catch (SQLException e){
            throw new MySQLException("Error occurred while finding todo items by assignee.", e);
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
    String query = "DELETE FROM todo_item WHERE todo_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, id);
        int rowsDeleted = preparedStatement.executeUpdate();
        return rowsDeleted > 0;
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while deleting todo item by id.", e);
    }
}

// In TodoItemsDaoImpl.java
@Override
public boolean deleteByAssignee(Person assignee) {
    String query = "DELETE FROM todo_item WHERE assignee_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, assignee.getId());
        preparedStatement.executeUpdate();
        return true;
    } catch (SQLException e) {
        throw new MySQLException("Error occurred while deleting todo items by assignee.", e);
    }
}


@Override
public TodoItem update(TodoItem model) {
    String query = "UPDATE todo_item SET title = ?, description = ?, done = ? WHERE todo_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, model.getTitle());
        preparedStatement.setString(2, model.getDescription());
        preparedStatement.setBoolean(3, model.isDone());
        preparedStatement.setInt(4, model.getId());
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
    int id = resultSet.getInt("todo_id");
    String title = resultSet.getString("title");
    String description = resultSet.getString("description");
    Timestamp timestamp = resultSet.getTimestamp("deadline");
    LocalDate deadline = timestamp.toLocalDateTime().toLocalDate();
    boolean done = resultSet.getBoolean("done");
    int assigneeId = resultSet.getInt("assignee_id");
    Person assignee = peopleDao.findById(assigneeId).orElseThrow(() -> new RuntimeException("Person not found in database"));
    return new TodoItem(id, title, description, deadline, done, assignee);
}
}
