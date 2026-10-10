package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;

import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.service.AccountService;

import java.util.Scanner;

@Component
public class AccountClose implements OperationCommand {

    private final Scanner scanner;
    private final AccountService accountService;

    public AccountClose(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("\nEnter account ID to close:");
        Long idAccount = null;

        try {
            idAccount = Long.parseLong(scanner.nextLine());

            accountService.accountClose(idAccount);
            System.out.println("Account with ID: " + idAccount + " has been closed.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println("Account with ID: " + idAccount + " has not been closed due to an error: " + e.getMessage());
        }

    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_CLOSE;
    }
}
