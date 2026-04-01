package Start.controller.dto;

import Start.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class BillResponseDTO {
    private Long billId;

    private BigDecimal amount;

    private Boolean isDefault;

    public BillResponseDTO(Bill bill){
        this.billId = bill.getBillId();
        this.amount = bill.getAmount();
        this.isDefault = bill.getIsDefault();
    }
}
