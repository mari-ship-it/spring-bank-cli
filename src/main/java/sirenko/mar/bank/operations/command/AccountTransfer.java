package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountTransfer implements OperationCommand {

    private final Scanner scanner;
    private final AccountService accountService;

    public AccountTransfer(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {

        Long fromAccountId;
        Long toAccountId;
        BigDecimal amount;

        System.out.println("Enter source account ID:");
        try {
            fromAccountId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");
            return;
        }

        System.out.println("Enter target account ID:");
        try {
            toAccountId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");
            return;
        }

        if (fromAccountId.equals(toAccountId)) {
            System.out.println("Source and target accounts must be different.");
            return;
        }

        System.out.println("Enter amount to transfer:");
        try {
            amount = new BigDecimal(scanner.nextLine());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("The transfer amount must be greater than zero");
                return;
            }
            accountService.accountTransfer(fromAccountId, toAccountId, amount);
            System.out.println("Amount " + amount
                    + " transferred from account ID " + fromAccountId +
                    " to account ID " + toAccountId);

        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid amount.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println("The translation was not performed. Reason: " + e.getMessage());
        }

    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_TRANSFER;
    }
}
