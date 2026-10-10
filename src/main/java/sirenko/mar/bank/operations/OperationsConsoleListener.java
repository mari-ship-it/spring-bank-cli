package sirenko.mar.bank.operations;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class OperationsConsoleListener {

    private final Map<OperationsType, OperationCommand> commandMap;
    private final Scanner scanner;

    public OperationsConsoleListener(List<OperationCommand> commands, Scanner scanner) {
        this.commandMap  = new HashMap<>();
        commands.forEach(command -> commandMap.put(command.operationsType(),
                command));
        this.scanner = scanner;
    }

    public void readFromConsole() {

        System.out.println("\nTo finish: EXIT");

        boolean running = true;

        while (running) {
            String meny = ("""
                    
                    Please enter one of operation type:
                    
                    - USER_CREATE
                    - SHOW_ALL_USERS
                    - ACCOUNT_CREATE
                    - ACCOUNT_CLOSE
                    - ACCOUNT_DEPOSIT
                    - ACCOUNT_TRANSFER
                    - ACCOUNT_WITHDRAW
                    """);

            System.out.println(meny);
            String commandInput = scanner.nextLine().trim().toUpperCase();

            try {
                OperationsType operationsType = OperationsType.valueOf(commandInput);

                if (operationsType == OperationsType.EXIT) {
                    return;
                }
                commandMap.get(operationsType).execute();

            } catch (IllegalArgumentException e) {
                System.out.println("\nUnknown operation type! Please try again");
            }
        }
    }

}
