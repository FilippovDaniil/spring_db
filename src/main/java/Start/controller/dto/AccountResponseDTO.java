package Start.controller.dto;

import Start.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class AccountResponseDTO {

    private Long accountId;

    private String name;

    private String email;

    private List<BillResponseDTO> bills;

    public AccountResponseDTO(Account account){
        this.accountId = account.getAccountId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.bills = account.getBiils()
                .stream().map(BillResponseDTO::new)
                .collect(Collectors.toList());
    }
}
