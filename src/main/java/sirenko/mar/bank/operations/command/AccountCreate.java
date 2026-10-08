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
public class AccountCreate implements  OperationCommand{

    private final Scanner scanner;
    private final SessionFactory sessionFactory;
    private final AccountProperties accountProperties;

    public AccountCreate (Scanner scanner, SessionFactory sessionFactory, AccountProperties accountProperties) {
        this.scanner = scanner;
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
    }


    @Override
    public void execute() {

        System.out.println("\nEnter the user ID for which to create an account:");
        Long idUserInput;

        try {
            idUserInput = Long.parseLong(scanner.nextLine().trim());

        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");
            return;
        }
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, idUserInput);

            if (user == null) {

                System.out.println("\nNo user with this Id exists, id: " + idUserInput);

            } else {

                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();

                    Account account = new Account(user, accountProperties.getDefaultAmount());
                    user.addAccount(account);

                    System.out.println("\nAccount created\n");

                    transaction.commit();

                } catch (Exception e) {

                    if (transaction != null) {
                        transaction.rollback();
                    }
                    System.out.println("\nFailed to create account id: " + idUserInput );
                    System.out.println("Reason: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_CREATE;
    }
}
