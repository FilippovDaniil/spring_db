package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
import Start.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositService {


    private final AccountService accountService;

    @Autowired
    public DepositService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Object deposit(Long accountId, BigDecimal amount){
        Account account = accountService.getById(accountId);
        Bill billDefault = AccountUtils.getBillFrom(account);
        billDefault.setAmount(billDefault.getAmount().add(amount));
        accountService.update(account);
        return "Success";
    }
}
