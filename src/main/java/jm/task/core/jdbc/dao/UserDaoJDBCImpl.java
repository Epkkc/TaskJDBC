package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {
        establishConnection();
    }

    @Override
    public void createUsersTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS users_table (" +
                "id INT(11) NOT NULL AUTO_INCREMENT," +
                "username VARCHAR(255) NOT NULL," +
                "last_name VARCHAR(255) NOT NULL," +
                "age INT(11) NOT NULL," +
                "PRIMARY KEY (id))" ;
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(sqlQuery);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String sqlQuery = "DROP TABLE IF EXISTS users_table";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(sqlQuery);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sqlQuery = "INSERT INTO users_table(username, last_name, age) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String sqlQuery = "DELETE FROM users_table WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            statement.setInt(1, (int) id);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users_table";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("username"),
                                   resultSet.getString("last_name"),
                                   resultSet.getByte("age")));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sqlQuery = "TRUNCATE TABLE users_table";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException throwable){
            throwable.printStackTrace();
        }
    }

    private void establishConnection() {
        try {
            connection = Util.getDBConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
