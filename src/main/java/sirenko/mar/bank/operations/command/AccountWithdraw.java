package sirenko.mar.bank.operations.command;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;

import java.util.Scanner;

@Component
public class AccountWithdraw implements OperationCommand {

    private final Scanner scanner;
    private final SessionFactory sessionFactory;

    public AccountWithdraw(Scanner scaner, SessionFactory sessionFactory) {
        this.scanner = scaner;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void execute() {

        Long idAccount;
        Double attemptedWithdraw;

        System.out.println("Enter account ID to withdraw from:");
        try {
            idAccount = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");
            return;
        }

        System.out.println("Enter amount to withdraw:");
        try {
            attemptedWithdraw = Double.parseDouble(scanner.nextLine());

            if (attemptedWithdraw <= 0.0) {
                System.out.println("The withdraw amount must be greater than zero");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid amount withdraw.");
            return;
        }

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Account account = session.find(Account.class, idAccount);

            if (account == null) {
                System.out.println("No account with ID: " + idAccount);
                transaction.rollback();
                return;
            }
            if (account.getMoneyAmount() < attemptedWithdraw) {
                System.out.println("No such money to withdraw from account, attempted withdraw: " + attemptedWithdraw);
                transaction.rollback();
                return;
            }

            account.setMoneyAmount(account.getMoneyAmount() - attemptedWithdraw);

            transaction.commit();
            System.out.println("The removal operation was completed successfully.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("The removal operation was not performed.");
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_WITHDRAW;
    }
}
