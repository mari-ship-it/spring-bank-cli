package sirenko.mar.bank.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import sirenko.mar.bank.AccountProperties;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.entity.User;

import java.util.List;

@Service
public class UserService {

    private final SessionFactory sessionFactory;
    private final AccountProperties accountProperties;

    public UserService ( SessionFactory sessionFactory, AccountProperties accountProperties) {
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
    }

    public void userCreate(String login) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            Long count = session.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.login = :loginParam", Long.class)
                    .setParameter("loginParam", login)
                    .uniqueResult();

            if (count != 0) {
                transaction.rollback();
                throw new IllegalArgumentException("The user already exists login: " + login);
            }

            User user = new User(login);
            Account account = new Account(user, accountProperties.getDefaultAmount());

            user.addAccount(account);
            session.persist(user);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<User> showAllUsers() {

        try (Session session = sessionFactory.openSession()) {

            return session.createQuery(
                            "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.accountList", User.class)
                    .list();
        }
    }

}
