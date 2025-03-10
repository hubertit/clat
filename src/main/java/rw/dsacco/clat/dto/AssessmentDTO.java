package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AssessmentDTO {
    private Long productId;
    private String loanApplicationNo;
    private BigDecimal loanApplicationAmount;
    private String status;
    private Long saccoId;       // ✅ Nullable
    private Double progress;     // ✅ Nullable
    private BigDecimal totalCost;    // ✅ Nullable
    private BigDecimal greenCost;    // ✅ Nullable
    private BigDecimal nonGreenCost; // ✅ Nullable
}
