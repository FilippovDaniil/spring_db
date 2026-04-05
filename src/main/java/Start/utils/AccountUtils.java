package Start.utils;

import Start.entity.Account;
import Start.entity.Bill;
import Start.exceptions.NotDefaultBillException;

public class AccountUtils {
    public static Bill getBillFrom(Account accountFrom) {
        return accountFrom.getBills().stream()
                .filter(Bill::getIsDefault)
                .findAny().orElseThrow(() -> new NotDefaultBillException("Default bill is not found for account with id: " + accountFrom.getAccountId()));
    }
}
