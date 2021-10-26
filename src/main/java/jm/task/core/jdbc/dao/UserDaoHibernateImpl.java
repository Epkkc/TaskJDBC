package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getDBSession();
    }

    @Override
    public void createUsersTable() {
        executeSQLQuery("CREATE TABLE IF NOT EXISTS User (" +
                "id BIGINT NOT NULL AUTO_INCREMENT," +
                "username VARCHAR(255) NOT NULL," +
                "last_name VARCHAR(255) NOT NULL," +
                "age TINYINT NOT NULL," +
                "PRIMARY KEY (id))");
    }

    @Override
    public void dropUsersTable() {
        executeSQLQuery("DROP TABLE IF EXISTS User");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            User user = session.load(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            users = session.createQuery("FROM User").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        executeSQLQuery("TRUNCATE TABLE User");
    }

    private void executeSQLQuery(String query) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
