package sirenko.mar.bank.operations.command;

import org.springframework.stereotype.Component;
import sirenko.mar.bank.operations.OperationCommand;
import sirenko.mar.bank.operations.OperationsType;

@Component
public class AccountDeposit implements OperationCommand {

    private final

    @Override
    public void execute() {



    }

    @Override
    public OperationsType operationsType() {
        return OperationsType.ACCOUNT_DEPOSIT;
    }
}
