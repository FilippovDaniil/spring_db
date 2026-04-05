package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
import Start.exceptions.NotDefaultBillException;
import Start.repository.AccountRepository;
import Start.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountService accountService;

    @Autowired
    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    public Object transfer(Long accountIdFrom, Long accountIdTo, BigDecimal amount){
        Account accountFrom = accountService.getById(accountIdFrom);
        Account accountTo = accountService.getById(accountIdTo);
        Bill billFrom = AccountUtils.getBillFrom(accountFrom);
        Bill billTo = AccountUtils.getBillFrom(accountTo);
        billFrom.setAmount(billFrom.getAmount().subtract(amount));
        billTo.setAmount(billTo.getAmount().add(amount));
        accountService.update(accountFrom);
        accountService.update(accountTo);
        return "Success";

    }


}
