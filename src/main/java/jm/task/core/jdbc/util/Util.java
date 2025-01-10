package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД

    //        String hostname = "localhost";
    private static final String userName = "root";
    private static final String password = "root";
    private static final String connectionURL = "jdbc:mysql://localhost:3306/usersdb";

    public static Connection getMyConnection() throws SQLException, ClassNotFoundException {
        Connection connection = DriverManager.getConnection(connectionURL, userName, password);
        return connection;
    }
}
