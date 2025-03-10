package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private Long id;
    private Long assessmentId;
    private Long questionId;
    private Long optionId;
    private Double cost;
    private Boolean isGreen;
}
