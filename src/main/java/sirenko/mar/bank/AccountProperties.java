package sirenko.mar.bank;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
//@Getter
public class AccountProperties {

    private final double defaultAmount;
    private final double transferCommission;

    public AccountProperties(
            @Value("${account.default-amount}") double defaultAmount,
            @Value("${account.transfer-commission}") double transferCommission
    ) {
        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
    }

    public double getDefaultAmount() {
        return this.defaultAmount;
    }

    public double getTransferCommission() {
        return this.transferCommission;
    }
}
