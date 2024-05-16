package se.lexicon.dao.impl;

import se.lexicon.Exception.MySQLException;
import se.lexicon.dao.PeopleDao;
import se.lexicon.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PeopleDaoImpl implements PeopleDao {
    private final Connection connection;

    public PeopleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Person> create(Person model) {

        String query = "INSERT INTO person(first_name,last_name) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, model.getFirstName());
            preparedStatement.setString(2, model.getLastName());
            int numberOfRowsInserted = preparedStatement.executeUpdate();
            if (numberOfRowsInserted == 0) {
                throw new MySQLException("Creating person failed.");
            }
            try (
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys()
            ) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    model = new Person(model.getId(), model.getFirstName(), model.getLastName());
                    return Optional.of(model);
                } else {
                    throw new MySQLException("Creating person failed.");
                }
            } catch (SQLException e) {
                throw new MySQLException("Error occurred while creating person.", e);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Person> findById(Integer id) {
        String query = "SELECT *FROM person WHERE person_id = ?";
        Person person= null;
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                person = getPerson(resultSet);
            }else {
                throw new MySQLException("Person with id:" + id+ " not found");
            }

        }catch (SQLException e){
            throw new MySQLException("Error getting connection");
        }
        return Optional.of(person);
    }

    @Override
    public Collection<Person> find(String firstname) {
        String query = "SELECT * FROM person WHERE first_name = ?";
        List<Person> personList= new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    personList.add(getPerson(resultSet));
                }
            }
        }catch (SQLException e){
            throw new MySQLException("Error getting connection");
        }
        return personList;
    }

    @Override
    public List<Person> findAll() {
        String query = "SELECT * FROM person";
        List<Person> personList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){try (ResultSet resultSet= preparedStatement.executeQuery()){
            while (resultSet.next()){
                personList.add(getPerson(resultSet));
            }
        }
        }catch (SQLException e){
            throw new MySQLException("Error getting connection");
        }
        return personList;
    }

    @Override
    public boolean deleteById(Integer id) {
        String query = "DELETE FROM person WHERE person_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setInt(1,id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0 ){
                System.out.println("Person deleted successfully");
                return true;
            }else {
                System.out.println("Person with id: " + id + " not found.");
            }
        }catch (SQLException e){
            throw new MySQLException("Error getting connection");
        }
        return false;
    }

    @Override
    public Person update(Person model) {
        String query = "UPDATE person SET first_name = ?, last_name = ? WHERE person_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, model.getFirstName());
            preparedStatement.setString(2, model.getLastName());
            preparedStatement.setInt(3, model.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update successful");
            } else {
                System.out.println("No person found with the given ID");
            }
        }catch (SQLException e) {
            System.out.println("Error getting connection");
        }
        return model;

    }

    private static Person getPerson(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("person_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        return new Person(id, firstName, lastName);
    }
}
