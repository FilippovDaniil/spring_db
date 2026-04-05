package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
import Start.exceptions.AccountNotFoundException;
import Start.exceptions.NotDefaultBillException;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(org.springframework.test.context.junit4.SpringRunner.class)
@SpringBootTest
@Transactional
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    private Long accountFromId;
    private Long accountToId;

    @Before
    public void setUp() {
        Bill billFrom = new Bill(BigDecimal.valueOf(1000), true);
        List<Bill> billsFrom = Arrays.asList(billFrom);
        accountFromId = accountService.save("Sender", "sender@example.com", billsFrom);

        Bill billTo = new Bill(BigDecimal.valueOf(500), true);
        List<Bill> billsTo = Arrays.asList(billTo);
        accountToId = accountService.save("Receiver", "receiver@example.com", billsTo);
    }

    @Test
    public void testTransfer() {
        Account beforeFrom = accountService.getById(accountFromId);
        Hibernate.initialize(beforeFrom.getBills());
        Bill beforeBillFrom = beforeFrom.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        BigDecimal amountFrom = beforeBillFrom.getAmount();

        Account beforeTo = accountService.getById(accountToId);
        Hibernate.initialize(beforeTo.getBills());
        Bill beforeBillTo = beforeTo.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        BigDecimal amountTo = beforeBillTo.getAmount();

        Object result = transferService.transfer(accountFromId, accountToId, BigDecimal.valueOf(200));

        assertEquals("Success", result);

        Account afterFrom = accountService.getById(accountFromId);
        Hibernate.initialize(afterFrom.getBills());
        Bill afterBillFrom = afterFrom.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);

        Account afterTo = accountService.getById(accountToId);
        Hibernate.initialize(afterTo.getBills());
        Bill afterBillTo = afterTo.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);

        assertEquals(amountFrom.subtract(BigDecimal.valueOf(200)), afterBillFrom.getAmount());
        assertEquals(amountTo.add(BigDecimal.valueOf(200)), afterBillTo.getAmount());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferFromNonExistentAccount() {
        transferService.transfer(99999L, accountToId, BigDecimal.valueOf(100));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferToNonExistentAccount() {
        transferService.transfer(accountFromId, 99999L, BigDecimal.valueOf(100));
    }
}