package rw.dsacco.clat.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentResponseDTO {
    private UUID code;
    private String customerName;
    private String productName; // ✅ Add productName as String
    private String loanApplicationNo;
    private BigDecimal loanApplicationAmount;
    private String doneByName;
    private String approvedByName;
    private String status;
    private LocalDateTime createdAt;  // ✅ Added createdAt
    private LocalDateTime updatedAt;  // ✅ Added updatedAt

}
