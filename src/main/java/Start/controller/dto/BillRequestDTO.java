package Start.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BillRequestDTO {
    private BigDecimal amount;

    private Boolean isDefault;
}
