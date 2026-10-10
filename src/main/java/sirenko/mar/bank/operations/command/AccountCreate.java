package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;

import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.service.AccountService;

import java.util.Scanner;

@Component
public class AccountCreate implements  OperationCommand{

    private final Scanner scanner;
    private final AccountService accountService;

    public AccountCreate (Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }


    @Override
    public void execute() {

        System.out.println("\nEnter the user ID for which to create an account:");
        Long idUser = null;

        try {
            idUser = Long.parseLong(scanner.nextLine().trim());

            accountService.accountCreate(idUser);
            System.out.println("\nAccount created\n");

        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid numeric ID.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

        } catch (Exception e) {
            System.out.println("\nFailed to create account id: " + idUser);
            System.out.println("Reason: " + e.getMessage());
        }

    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_CREATE;
    }
}
