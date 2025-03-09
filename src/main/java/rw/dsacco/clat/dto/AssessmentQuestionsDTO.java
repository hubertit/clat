package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssessmentQuestionsDTO {
    private Long activityId;
    private String enQuestion;
    private String frQuestion;
    private String knQuestion;
    private String description;
}
