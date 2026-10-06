package sirenko.mar.bank.operations;

import java.util.Scanner;

public interface OperationCommand {

    void execute();
    OperationsType operationsType();
}
