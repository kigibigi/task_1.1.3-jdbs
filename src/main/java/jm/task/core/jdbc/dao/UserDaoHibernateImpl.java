package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

//    получение сессии
    private static Session getSession() {
        return Util.getSessionFactory().openSession();
    }

    private static final String CREATE_USER_TABLE_SQL = """
            CREATE TABLE user(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(30),
                last_name VARCHAR(35),
                age INT
            )
            """;

    private static final String FIND_ALL_USER_SQL = """
            SELECT * FROM user
            """;
    @Override
    public void createUsersTable() {
        try {
            Session session = getSession();

            session.beginTransaction();
            session.createSQLQuery(CREATE_USER_TABLE_SQL).executeUpdate();
            session.getTransaction().commit();
        } catch (PersistenceException ex) {
            System.out.println("Table 'user' already exists");
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            Session session = getSession();

            session.beginTransaction();

            session.createSQLQuery("DROP TABLE user").executeUpdate();

            session.getTransaction().commit();
//            System.out.println("Table delete!");
        } catch (PersistenceException ex) {
            System.out.println("Table is not exist");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);

        Session session = getSession();

        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    @Override
    public void removeUserById(long id) {
        Session session = getSession();

        session.beginTransaction();
        User removeUser = (User) session.find(User.class, id);
        session.remove(removeUser);

        session.getTransaction().commit();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = getSession();
        List<User> users = new ArrayList<>();

        session.beginTransaction();

        users = (List<User>) session.createQuery("from User order by id").list();

        session.getTransaction().commit();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = getSession();

        session.beginTransaction();

        session.createQuery("DELETE FROM User").executeUpdate();

        session.getTransaction().commit();
    }
}
