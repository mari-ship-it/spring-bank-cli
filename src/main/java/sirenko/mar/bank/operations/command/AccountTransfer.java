package sirenko.mar.bank.operations.command;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import sirenko.mar.bank.AccountProperties;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;

import java.util.Scanner;

@Component
public class AccountTransfer implements OperationCommand {

    private final Scanner scanner;
    private final SessionFactory sessionFactory;
    private final AccountProperties accountProperties;

    public AccountTransfer(Scanner scanner, SessionFactory sessionFactory, AccountProperties accountProperties) {
        this.scanner = scanner;
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
    }

    @Override
    public void execute() {

        Long sourceId;
        Long targetId;
        Double transferAmount;

        System.out.println("Enter source account ID:");
        try {
            sourceId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");
            return;
        }

        System.out.println("Enter target account ID:");
        try {
            targetId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");
            return;
        }

        System.out.println("Enter amount to transfer:");
        try {
            transferAmount = Double.parseDouble(scanner.nextLine());

            if (transferAmount <= 0.0) {
                System.out.println("The withdraw amount must be greater than zero");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid amount.");
            return;
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Account accountSource = session.find(Account.class, sourceId);
            if (accountSource == null) {
                System.out.println("No account with ID: " + sourceId);
                transaction.rollback();
                return;
            }

            if (accountSource.getMoneyAmount() < transferAmount) {
                System.out.println("No such money to transfer from account, attempted transfer: " + transferAmount);
                transaction.rollback();
                return;
            }

            Account accountTarget = session.find(Account.class, targetId);

            if (accountTarget == null) {
                System.out.println("No account with ID: " + targetId);
                transaction.rollback();
                return;
            }

            if (accountSource.getUser().getId().equals(accountTarget.getUser().getId())) {

                accountSource.setMoneyAmount(accountSource.getMoneyAmount() - transferAmount);
                accountTarget.setMoneyAmount(accountTarget.getMoneyAmount() + transferAmount);
            } else {

                Double transferAmountAndFee = transferAmount * (1 + accountProperties.getTransferCommission());
                if (accountSource.getMoneyAmount() < transferAmountAndFee) {
                    System.out.println("No such money to transfer from account, attempted transfer: " + transferAmount);
                    transaction.rollback();
                    return;
                }
                accountSource.setMoneyAmount(accountSource.getMoneyAmount() - transferAmountAndFee);
                accountTarget.setMoneyAmount(accountTarget.getMoneyAmount() + transferAmount);
            }

            transaction.commit();
            System.out.println("Amount " + transferAmount
                            + " transferred from account ID " + sourceId +
                            " to account ID " + targetId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("The translation was not performed.");
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_TRANSFER;
    }
}
