package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
            // ???
            if(ex.getCause().getCause().getMessage().equals("Table 'user' already exists")) {

            } else {
                throw ex;
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createSQLQuery("DROP TABLE user").executeUpdate();

            session.getTransaction().commit();
        } catch (Exception ex) {
            // ???
            if(ex.getCause().getCause().getMessage().startsWith("Unknown table")) {

            } else {
                throw ex;
            }
        }
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = sessionFactory.openSession()) {
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

        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(user);
                transaction.commit();
            } catch (Exception ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw ex;
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User removeUser = (User) session.find(User.class, id);
                session.remove(removeUser);
                transaction.commit();
            } catch (Exception ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw ex;
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                users = (List<User>) session.createQuery("from User order by id").list();

                session.getTransaction().commit();
                System.out.println(transaction.toString());
            } catch (Exception ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw ex;
            }
            return users;
        }
    }
}
