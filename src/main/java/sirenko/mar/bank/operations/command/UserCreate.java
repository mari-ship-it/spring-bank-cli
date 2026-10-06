package sirenko.mar.bank.operations.command;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import sirenko.mar.bank.AccountProperties;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.entity.User;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;

import java.util.Scanner;

@Component
public class UserCreate implements OperationCommand {

    private final SessionFactory sessionFactory;
    private final Scanner scanner;
    private final AccountProperties accountProperties;

    public UserCreate (SessionFactory sessionFactory, Scanner scanner, AccountProperties accountProperties) {
        this.sessionFactory = sessionFactory;
        this.scanner = scanner;
        this.accountProperties = accountProperties;
    }

    @Override
    public void execute() {

        System.out.println("Enter login for new user:");
        String login = scanner.nextLine().trim();

        if (login.isEmpty()) {
            System.out.println("\nLogin cannot be empty! Please try again.");
            return;
        }

        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.login = :loginParam", Long.class)
                    .setParameter("loginParam", login)
                    .uniqueResult();

            if (count != 0) {
                System.out.println("\nThe user already exists login: " + login);
            } else {
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    User user = new User(login);
                    Account account = new Account(user, accountProperties.getDefaultAmount());

                    user.addAccount(account);
                    session.persist(user);

                    System.out.println("User created: " + user);

                    transaction.commit();

                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    System.out.println("\nFailed to create user login: " + login);
                    System.out.println("Reason: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.USER_CREATE;
    }
}
