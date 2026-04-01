package Start.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequestDTO {

    @JsonProperty("account_id_from")
    private Long accountIdFrom;

    @JsonProperty("account_id_to")
    private Long accountIdTo;

    @JsonProperty("amount")
    private BigDecimal amount;


}
