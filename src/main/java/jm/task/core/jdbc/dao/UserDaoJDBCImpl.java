package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_USER_TABLE_SQL = """
            CREATE TABLE user(
                id          INT PRIMARY KEY AUTO_INCREMENT,
                name        VARCHAR(30),
                last_name   VARCHAR(35),
                age         INT
            )
            """;

    private static final String DROP_USER_TABLE_SQL = """
            DROP TABLE user;
            """;

    private static final String SAVE_USER_SQL = """
            INSERT INTO user(name, last_name, age)
            VALUES (?, ?, ?)
            """;

    private static final String FIND_ALL_USER_SQL = """
            SELECT * FROM user
            """;

    private static final String CLEAN_USER_TABLE_SQL = """
            TRUNCATE user;
            """;

    private static final String DELETE_SQL = """
            DELETE FROM user
            WHERE id = ?
            """;

    public UserDaoJDBCImpl() {
    }

    Connection connection = Util.getMyConnection();

    public void createUsersTable() {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(CREATE_USER_TABLE_SQL))
        {
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Таблица 'user' уже существует");
        }
    }

    public void dropUsersTable() {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(DROP_USER_TABLE_SQL))
        {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Таблицы 'user' не существует");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SAVE_USER_SQL))
        {
            //устанавливаю значения для полей
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            //исполнение запроса
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(DELETE_SQL))
        {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
}

    public List<User> getAllUsers() {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(FIND_ALL_USER_SQL))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static User buildUser(ResultSet resultSet) throws SQLException {
        long id = resultSet.getByte(1);

        User user = new User(
                resultSet.getString("name"),
                resultSet.getString("last_name"),
                resultSet.getByte("age")
        );

        user.setId(id);
        return user;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(CLEAN_USER_TABLE_SQL))
        {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
