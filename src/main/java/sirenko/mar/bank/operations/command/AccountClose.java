package sirenko.mar.bank.operations.command;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.entity.User;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;

import java.util.Scanner;

@Component
public class AccountClose implements OperationCommand {

    private final Scanner scanner;
    private final SessionFactory sessionFactory;

    public AccountClose(Scanner scanner, SessionFactory sessionFactory) {
        this.scanner = scanner;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void execute() {
        System.out.println("\nEnter account ID to close:");
        Long idAccount;

        try {
            idAccount = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");
            return;
        }

        try (Session session = sessionFactory.openSession()) {
            Account account = session.find(Account.class, idAccount);

            if (account == null) {
                System.out.println("No account with ID: " + idAccount);
                return;
            }

            User user = account.getUser();

            if (user.getAccountList().size() <= 1) {
                System.out.println(
                        "The account with ID " + idAccount
                                + " cannot be closed because it is the only account of the user.");
                return;
            }
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                if (account.getMoneyAmount() > 0.0) {

                    for (Account userAccount : user.getAccountList()) {
                        if (!userAccount.getId().equals(account.getId())) {
                            userAccount.setMoneyAmount(
                                    userAccount.getMoneyAmount() + account.getMoneyAmount());
                            session.merge(userAccount);
                            break;
                        }
                    }
                }
                user.getAccountList().remove(account);
                session.remove(account);
                transaction.commit();
                System.out.println("Account with ID: " + idAccount + " has been closed.");

            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                System.out.println("Account with ID: " + idAccount + " has not been closed due to an error: " + e.getMessage());
            }
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_CLOSE;
    }
}
