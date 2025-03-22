package rw.dsacco.clat.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnsweredQuestionDTO {
    private String questionCode;
    private String enQuestion;
    private String frQuestion;
    private String knQuestion;
    private String description;
    private Long optionId;
    private BigDecimal cost;
    private Boolean isGreen;
}
