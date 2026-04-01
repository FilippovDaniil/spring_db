package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
import Start.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {


    private final AccountService accountService;

    @Autowired
    public PaymentService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Object pay(Long accountId, BigDecimal amount){
        Account account = accountService.getById(accountId);
        Bill billDefault = AccountUtils.getBillFrom(account);
        billDefault.setAmount(billDefault.getAmount().subtract(amount));
        accountService.update(account);
        return "Success";
    }
}
