package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
import Start.exceptions.AccountNotFoundException;
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
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    private Long testAccountId;

    @Before
    public void setUp() {
        Bill bill = new Bill(BigDecimal.valueOf(1000), true);
        List<Bill> bills = Arrays.asList(bill);
        testAccountId = accountService.save("Payment Test User", "payment@example.com", bills);
    }

    @Test
    public void testPay() {
        Account beforeAccount = accountService.getById(testAccountId);
        Hibernate.initialize(beforeAccount.getBills());
        Bill beforeBill = beforeAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        BigDecimal initialAmount = beforeBill.getAmount();

        Object result = paymentService.pay(testAccountId, BigDecimal.valueOf(300));

        assertEquals("Success", result);

        Account afterAccount = accountService.getById(testAccountId);
        Hibernate.initialize(afterAccount.getBills());
        Bill afterBill = afterAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        
        assertEquals(initialAmount.subtract(BigDecimal.valueOf(300)), afterBill.getAmount());
    }

    @Test
    public void testPayFullAmount() {
        Bill bill = new Bill(BigDecimal.valueOf(100), true);
        List<Bill> bills = Arrays.asList(bill);
        Long fullPayAccountId = accountService.save("Full Pay User", "fullpay@example.com", bills);

        Object result = paymentService.pay(fullPayAccountId, BigDecimal.valueOf(100));

        assertEquals("Success", result);

        Account afterAccount = accountService.getById(fullPayAccountId);
        Hibernate.initialize(afterAccount.getBills());
        Bill afterBill = afterAccount.getBills().stream()
                .filter(Bill::getIsDefault)
                .findFirst().orElseThrow(RuntimeException::new);
        
        assertEquals(BigDecimal.ZERO, afterBill.getAmount());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testPayNonExistentAccount() {
        paymentService.pay(99999L, BigDecimal.valueOf(100));
    }
}