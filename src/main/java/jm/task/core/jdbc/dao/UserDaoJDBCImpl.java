package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getDBConnection();
    }

    @Override
    public void createUsersTable() {
        executeSQLUpdate("CREATE TABLE IF NOT EXISTS users_table (" +
                "id BIGINT NOT NULL AUTO_INCREMENT," +
                "username VARCHAR(255) NOT NULL," +
                "last_name VARCHAR(255) NOT NULL," +
                "age TINYINT NOT NULL," +
                "PRIMARY KEY (id))");
    }

    @Override
    public void dropUsersTable() {
        executeSQLUpdate("DROP TABLE IF EXISTS users_table");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeSQLUpdate("INSERT INTO users_table (username, last_name, age) " +
                "VALUES (\"" + name + "\",\"" + lastName + "\"," + age + ")");
    }

    @Override
    public void removeUserById(long id) {
        executeSQLUpdate("DELETE FROM users_table WHERE id = " + id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Failure while creating a statement");
            e.printStackTrace();
        }
        if (statement != null) {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM users_table");
                while (resultSet.next()) {
                    users.add(new User(resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("last_name"),
                            resultSet.getByte("age")));
                }
            } catch (SQLException e) {
                System.out.println("Failure while executing query");
                e.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Failure while closing statement");
                    e.printStackTrace();
                }
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        executeSQLUpdate("TRUNCATE TABLE users_table");
    }

    private void executeSQLUpdate(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Failure while creating a statement");
            e.printStackTrace();
        }
        if (statement != null) {
            try {
                statement.executeUpdate(query);
                connection.commit();
            } catch (SQLException e) {
                System.out.println("Failure while executing query");
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Failure while closing statement");
                    e.printStackTrace();
                }
            }
        }
    }

}
