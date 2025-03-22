package rw.dsacco.clat.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private Long saccoId;
    private Double progress;
    private BigDecimal totalCost;
    private BigDecimal greenCost;
    private BigDecimal nonGreenCost;
    private String customerName;
    private String customerGender;
    private List<AnsweredQuestionDTO> questions;
}
