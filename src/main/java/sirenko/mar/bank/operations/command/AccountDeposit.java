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
public class AccountDeposit implements OperationCommand {

    private final Scanner scanner;
    private final SessionFactory sessionFactory;

    public AccountDeposit(Scanner scanner, SessionFactory sessionFactory) {
        this.scanner = scanner;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void execute() {

        System.out.println("Enter account ID:");
        Long accountId;
        double moneyAmount;

        try {
            accountId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");
            return;
        }
        System.out.println("Enter amount to deposit:");

        try {
             moneyAmount = Double.parseDouble(scanner.nextLine().trim());

            if (moneyAmount <= 0.0) {
                System.out.println("The deposit amount must be greater than zero");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid amount deposited.");
            return;
        }
        try (Session session = sessionFactory.openSession()) {

            Account account = session.find(Account.class, accountId);

            if (account == null) {
                System.out.println("No account with ID: " + accountId);
            } else {

                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();

                    account.setMoneyAmount(account.getMoneyAmount() + moneyAmount);

                    System.out.println("\nAmount " + moneyAmount + " deposited to account ID: " + accountId);
                    transaction.commit();

                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    System.out.println("\nAmount has not been deposited into the account with ID: " + accountId );
                    System.out.println("Reason: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_DEPOSIT;
    }
}
