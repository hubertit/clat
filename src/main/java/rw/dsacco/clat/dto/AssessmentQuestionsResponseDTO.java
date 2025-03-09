package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssessmentQuestionsResponseDTO {
    private Long id;
    private String code;
    private String enQuestion;
    private String frQuestion;
    private String knQuestion;
    private String description;

    public AssessmentQuestionsResponseDTO(Long id, String code, String enQuestion, String frQuestion, String knQuestion, String description) {
        this.id = id;
        this.code = code;
        this.enQuestion = enQuestion;
        this.frQuestion = frQuestion;
        this.knQuestion = knQuestion;
        this.description = description;
    }
}
