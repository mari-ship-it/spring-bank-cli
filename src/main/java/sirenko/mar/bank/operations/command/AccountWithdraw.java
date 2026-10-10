package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountWithdraw implements OperationCommand {

    private final Scanner scanner;
    private final AccountService accountService;

    public AccountWithdraw(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {

        Long idAccount;
        BigDecimal attemptedWithdraw;

        System.out.println("Enter account ID to withdraw from:");
        try {
            idAccount = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");
            return;
        }

        System.out.println("Enter amount to withdraw:");
        try {
            attemptedWithdraw = new BigDecimal(scanner.nextLine());

            if (attemptedWithdraw.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("The withdraw amount must be greater than zero");
                return;
            }
            accountService.accountWithdraw(idAccount, attemptedWithdraw);
            System.out.println("The removal operation was completed successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid amount withdraw.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println("The removal operation was not performed. Reason: " + e.getMessage());
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_WITHDRAW;
    }
}
