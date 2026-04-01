package Start.controller;

import Start.controller.dto.AccountRequestDTO;
import Start.controller.dto.AccountResponseDTO;
import Start.controller.dto.BillRequestDTO;
import Start.entity.Bill;
import Start.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}")
    public AccountResponseDTO getById(@PathVariable Long id){
        return new AccountResponseDTO(accountService.getById(id));
    }

    @PostMapping("/accounts")
    public Long create(@RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.save(accountRequestDTO.getName(),
                accountRequestDTO.getEmail(),
                (accountRequestDTO.getBills() == null ? Collections.<BillRequestDTO>emptyList() : accountRequestDTO.getBills())
                        .stream()
                        .map(billRequestDTO -> new Bill(billRequestDTO.getAmount(),billRequestDTO.getIsDefault()))
                        .collect(Collectors.toList()));
    }
}
