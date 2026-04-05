package Start.controller;

import Start.controller.dto.AccountRequestDTO;
import Start.controller.dto.BillRequestDTO;
import Start.controller.dto.AccountResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(org.springframework.test.context.junit4.SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateAccount() {
        AccountRequestDTO request = new AccountRequestDTO();
        request.setName("Controller Test User");
        request.setEmail("controller@example.com");

        BillRequestDTO bill = new BillRequestDTO();
        bill.setAmount(BigDecimal.valueOf(1000));
        bill.setIsDefault(true);
        request.setBills(Arrays.asList(bill));

        ResponseEntity<Long> response = restTemplate.postForEntity("/accounts", request, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetAccountById() {
        // Сначала создаем аккаунт
        AccountRequestDTO request = new AccountRequestDTO();
        request.setName("Get Test User");
        request.setEmail("getcontroller@example.com");

        BillRequestDTO bill = new BillRequestDTO();
        bill.setAmount(BigDecimal.valueOf(500));
        bill.setIsDefault(true);
        request.setBills(Arrays.asList(bill));

        Long accountId = restTemplate.postForEntity("/accounts", request, Long.class).getBody();

        // Получаем аккаунт
        ResponseEntity<AccountResponseDTO> response = restTemplate.getForEntity(
                "/accounts/" + accountId, AccountResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Get Test User", response.getBody().getName());
        assertEquals("getcontroller@example.com", response.getBody().getEmail());
        assertEquals(1, response.getBody().getBills().size());
    }

    @Test
    public void testGetAccountNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/accounts/99999", String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}