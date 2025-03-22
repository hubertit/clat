package rw.dsacco.clat.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentResponseDTO {
    private Long id;
    private UUID code;
    private String productName;
    private String loanApplicationNo;
    private BigDecimal loanApplicationAmount;
    private String status;
    private LocalDateTime createdAt;
    private Long saccoId;            // ✅ Nullable
    private Double progress;         // ✅ Nullable
    private BigDecimal totalCost;    // ✅ Nullable
    private BigDecimal greenCost;    // ✅ Nullable
    private BigDecimal nonGreenCost; // ✅ Nullabl
    private String customerName;     // ✅ New
    private String customerGender;   // ✅ New
}
