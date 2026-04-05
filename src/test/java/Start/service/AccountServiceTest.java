package Start.service;

import Start.entity.Account;
import Start.entity.Bill;
import Start.exceptions.AccountNotFoundException;
import lombok.var;
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
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testSaveAccount() {
        Bill bill = new Bill(BigDecimal.valueOf(1000), true);
        List<Bill> bills = Arrays.asList(bill);

        Long accountId = accountService.save("Test User", "test@example.com", bills);

        assertNotNull(accountId);
        Account account = accountService.getById(accountId);
        int size = account.getBills().size();
        assertEquals("Test User", account.getName());
        assertEquals("test@example.com", account.getEmail());
        assertEquals(1, size);
    }

    @Test
    public void testGetAccountById() {
        Bill bill = new Bill(BigDecimal.valueOf(500), true);
        List<Bill> bills = Arrays.asList(bill);
        Long accountId = accountService.save("Get Test", "get@example.com", bills);

        Account account = accountService.getById(accountId);

        assertNotNull(account);
        assertEquals(accountId, account.getAccountId());
        assertEquals("Get Test", account.getName());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetAccountByIdNotFound() {
        accountService.getById(99999L);
    }

    @Test
    public void testUpdateAccount() {
        Bill bill = new Bill(BigDecimal.valueOf(200), true);
        List<Bill> bills = Arrays.asList(bill);
        Long accountId = accountService.save("Update Test", "update@example.com", bills);

        Account account = accountService.getById(accountId);
        account.setName("Updated Name");
        
        Account updatedAccount = accountService.update(account);

        assertEquals("Updated Name", updatedAccount.getName());
        
        Account fetchedAccount = accountService.getById(accountId);
        assertEquals("Updated Name", fetchedAccount.getName());
    }
}