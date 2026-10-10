package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;
import sirenko.mar.bank.entity.User;
import sirenko.mar.bank.service.UserService;

import java.util.List;

@Component
public class ShowAllUsers implements OperationCommand {

    private final UserService userService;

    public ShowAllUsers (UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {

        try {
            List<User> users = userService.showAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users found in the database.");
                return;
            }

            users.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Failed to load users. Reason: " + e.getMessage());
        }

    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.SHOW_ALL_USERS;
    }

}
