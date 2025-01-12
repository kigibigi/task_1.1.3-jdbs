package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    SessionFactory sessionFactory = Util.getSessionFactory();
    private static final String CREATE_USER_TABLE_SQL = """
            CREATE TABLE user
            (
                id          INT PRIMARY KEY AUTO_INCREMENT,
                name        VARCHAR(30),
                last_name   VARCHAR(35),
                age         INT
            )
            """;

    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createSQLQuery(CREATE_USER_TABLE_SQL).executeUpdate();
            session.getTransaction().commit();

        } catch (PersistenceException ex) {
            System.out.println("Table 'user' already exists");
            session.getTransaction().rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.createSQLQuery("DROP TABLE user").executeUpdate();

            session.getTransaction().commit();
        } catch (PersistenceException ex) {
            System.out.println("Table is not exist");
            session.getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            User removeUser = (User) session.find(User.class, id);
            session.remove(removeUser);

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        List<User> users = new ArrayList<>();

        session.beginTransaction();

        users = (List<User>) session.createQuery("from User order by id").list();

        session.getTransaction().commit();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.createQuery("DELETE FROM User").executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        }
    }
}
