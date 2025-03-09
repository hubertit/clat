package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class AssessmentDTO {
    private Long customerId;
    private Long productId; // ✅ Keeping productId as a simple Long
    private String loanApplicationNo;
    private BigDecimal loanApplicationAmount;
    private Long doneBy;
    private String status;
}
