package sirenko.mar.bank.operations;

public interface OperationCommand {

    void execute();
    OperationsType operationsType();
}
