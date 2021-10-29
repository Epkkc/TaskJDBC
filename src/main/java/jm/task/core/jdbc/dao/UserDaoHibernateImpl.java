package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


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
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                session.getTransaction().rollback();
            } catch (Exception e1) {
                e.addSuppressed(e1);
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception e){
                System.out.println("Failure while closing resource");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("DELETE User WHERE id = :id");
            query.setParameter("id", id);
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                session.getTransaction().rollback();
            } catch (Exception e1) {
                e.addSuppressed(e1);
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception e){
                System.out.println("Failure while closing resource");
                e.printStackTrace();
            }
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
            try {
                session.getTransaction().rollback();
            } catch (Exception e1) {
                e.addSuppressed(e1);
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception e){
                System.out.println("Failure while closing resource");
                e.printStackTrace();
            }
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
            try {
                session.getTransaction().rollback();
            } catch (Exception e1) {
                e.addSuppressed(e1);
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception e){
                System.out.println("Failure while closing resource");
                e.printStackTrace();
            }
        }
    }
}
