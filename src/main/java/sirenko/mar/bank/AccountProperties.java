package sirenko.mar.bank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class AccountProperties {

    private final BigDecimal defaultAmount;
    private final BigDecimal transferCommission;

    public AccountProperties(
            @Value("${account.default-amount}") BigDecimal defaultAmount,
            @Value("${account.transfer-commission}") BigDecimal transferCommission
    ) {
        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
    }

    public BigDecimal getDefaultAmount() {
        return this.defaultAmount;
    }

    public BigDecimal getTransferCommission() {
        return this.transferCommission;
    }
}
