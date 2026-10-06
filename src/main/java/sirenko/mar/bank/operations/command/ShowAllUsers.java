package sirenko.mar.bank.operations.command;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.entity.User;

import java.util.List;
import java.util.Scanner;

@Component
public class ShowAllUsers implements OperationCommand {

    private final SessionFactory sessionFactory;

    public ShowAllUsers(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void execute() {

        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery(
                    "FROM User u LEFT JOIN FETCH u.accountList", User.class)
                    .list();

            if (users.isEmpty()) {
                System.out.println("\nNo users found in the database.");
            } else {
                users.forEach(System.out::println);
            }
        }

    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.SHOW_ALL_USERS;
    }

}
