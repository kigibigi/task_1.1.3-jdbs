package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserService userService = new UserServiceImpl();

        // Создание таблицы User(ов)
        userService.createUsersTable();
        System.out.println("Таблица создана");

        // Добавление 4 User(ов) в таблицу

        List<User> users = new ArrayList<>();
        users.add(new User("Sergey", "Petrov", (byte) 19));
        users.add(new User("Ivan", "Ivanov", (byte) 33));
        users.add(new User("Klara", "Mironova", (byte) 12));
        users.add(new User("Proskoviya", "Prokovieva", (byte) 58));

        users.forEach(
                user -> {
                    userService.saveUser(user.getName(), user.getLastName(), user.getAge());
                    System.out.println("Добавлен " + user.getLastName());
                });

        // Получение всех User из базы и вывод в консоль
        System.out.println();
        List<User> allUsers = userService.getAllUsers();
        allUsers.forEach(System.out::println);

        // Очистка таблицы User(ов)
        System.out.println();
        userService.cleanUsersTable();

        // Удаление таблицы
        userService.dropUsersTable();
    }
}
