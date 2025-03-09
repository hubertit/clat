package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseOptionsDTO {
    private String questionCode;
    private String enOption;
    private String frOption;
    private String knOption;
    private boolean isGreen;
    private String description;
}
