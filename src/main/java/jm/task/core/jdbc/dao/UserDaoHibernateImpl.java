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

    private SessionFactory sessionFactory = new Util().getSessionFactory();

    private static final String CREATE_USER_TABLE_SQL = """
            CREATE TABLE user
            (
                id          INT PRIMARY KEY AUTO_INCREMENT,
                name        VARCHAR(30),
                last_name   VARCHAR(35),
                age         INT
            )
            """;

    // DDL - CREATE, DROP, TRUNCATE
    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.createSQLQuery(CREATE_USER_TABLE_SQL).executeUpdate();
            session.getTransaction().commit();

        } catch (Exception ex) {
            // ignore
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createSQLQuery("DROP TABLE user").executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
//            (PersistenceException ex)
        }
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = sessionFactory.openSession())
        {
            session.beginTransaction();

            session.createQuery("DELETE FROM User").executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        }
    }

    // DML - SELECT, INSERT, UPDATE, DELETE
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
        } finally {
            session.close();
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
            throw ex;
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            users = (List<User>) session.createQuery("from User order by id").list();

            session.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
        return users;
    }
}
