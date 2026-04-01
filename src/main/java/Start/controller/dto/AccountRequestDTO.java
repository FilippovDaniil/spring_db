package Start.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class AccountRequestDTO {
    private String name;

    private String email;

    @JsonProperty("bills")
    @JsonAlias("biils")
    private List<BillRequestDTO> bills;
}
