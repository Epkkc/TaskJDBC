package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>(
                Arrays.asList(
                    new User("first_name", "first_lastName", (byte) 10),
                    new User("second_name", "second_lastName", (byte) 20),
                    new User("third_name", "third_lastName", (byte) 30),
                    new User("fourth_name", "fourth_lastName", (byte) 40)));

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        for (User user : users) {
            userService.saveUser(user);
            System.out.println("User с именем - " + user.getName() + " добавлен в базу данных");
        }

        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            System.out.println(user);
        }

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
