package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StagesDTO {
    private Long productId;
    private String enStage;
    private String frStage;
    private String knStage;
    private String description;
}
