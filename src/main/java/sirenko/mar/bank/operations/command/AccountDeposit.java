package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountDeposit implements OperationCommand {

    private final Scanner scanner;
    private final AccountService accountService;

    public AccountDeposit(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {

        Long idAccount;
        BigDecimal moneyAmount;

        System.out.println("Enter account ID:");
        try {
            idAccount = Long.parseLong(scanner.nextLine().trim());

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");
            return;
        }

        System.out.println("Enter amount to deposit:");
        try {
             moneyAmount = new  BigDecimal(scanner.nextLine().trim());

            if (moneyAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("The deposit amount must be greater than zero");
                return;
            }

            accountService.accountDeposit(idAccount, moneyAmount);
            System.out.println("Amount " + moneyAmount + " deposited to account ID: " + idAccount);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid amount deposited.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println("Amount has not been deposited into the account with ID: " + idAccount);
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_DEPOSIT;
    }
}
