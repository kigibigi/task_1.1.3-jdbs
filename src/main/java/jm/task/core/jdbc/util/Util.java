package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД

    public static Connection getMyConnection() throws SQLException, ClassNotFoundException {
//        String hostname = "localhost";
        String userName = "root";
        String password = "root";

        String connectionURL = "jdbc:mysql://localhost:3306/usersdb";

        Connection connection = DriverManager.getConnection(connectionURL, userName, password);

        return connection;
    }
}
