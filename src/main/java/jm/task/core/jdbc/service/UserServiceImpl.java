package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static final UserDaoJDBCImpl daoUser = new UserDaoJDBCImpl();

    public void createUsersTable() {
        daoUser.createUsersTable();
    }

    public void dropUsersTable() {
        daoUser.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        daoUser.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        daoUser.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return daoUser.getAllUsers();
    }

    public void cleanUsersTable() {
        daoUser.cleanUsersTable();
    }
}
