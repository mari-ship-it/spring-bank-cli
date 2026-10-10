package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;

import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.service.UserService;

import java.util.Scanner;

@Component
public class UserCreate implements OperationCommand {

    private final Scanner scanner;
    private final UserService userService;

    public UserCreate (Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void execute() {

        System.out.println("Enter login for new user:");
        String login = scanner.nextLine().trim();

        if (login.isEmpty()) {
            System.out.println("\nLogin cannot be empty! Please try again.");
            return;
        }
        try {
            userService.userCreate(login);
            System.out.println("User successfully created with login: " + login);

        } catch (IllegalArgumentException e) {
            System.out.println("\nFailed to create user. " + e.getMessage());

        } catch (Exception e) {
            System.out.println("\nFailed to create user login: " + login);
            System.out.println("Reason: " + e.getMessage());
        }
    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.USER_CREATE;
    }
}
