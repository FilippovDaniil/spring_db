package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
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
public class DepositServiceTest {

    @Autowired
    private DepositService depositService;

    @Autowired
    private AccountService accountService;

    private Long testAccountId;

    @Before
    public void setUp() {
        Bill bill = new Bill(BigDecimal.valueOf(1000), true);
        List<Bill> bills = Arrays.asList(bill);
        testAccountId = accountService.save("Deposit Test User", "deposit@example.com", bills);
    }

    @Test
    public void testDeposit() {
        Account beforeAccount = accountService.getById(testAccountId);
        Hibernate.initialize(beforeAccount.getBills());
        Bill beforeBill = beforeAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        BigDecimal initialAmount = beforeBill.getAmount();

        Object result = depositService.deposit(testAccountId, BigDecimal.valueOf(500));

        assertEquals("Success", result);

        Account afterAccount = accountService.getById(testAccountId);
        Hibernate.initialize(afterAccount.getBills());
        Bill afterBill = afterAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        
        assertEquals(initialAmount.add(BigDecimal.valueOf(500)), afterBill.getAmount());
    }

    @Test
    public void testDepositZeroAmount() {
        Account beforeAccount = accountService.getById(testAccountId);
        Hibernate.initialize(beforeAccount.getBills());
        Bill beforeBill = beforeAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        BigDecimal initialAmount = beforeBill.getAmount();

        depositService.deposit(testAccountId, BigDecimal.ZERO);

        Account afterAccount = accountService.getById(testAccountId);
        Hibernate.initialize(afterAccount.getBills());
        Bill afterBill = afterAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        
        assertEquals(initialAmount, afterBill.getAmount());
    }
}