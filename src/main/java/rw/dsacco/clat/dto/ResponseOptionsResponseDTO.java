package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseOptionsResponseDTO {
    private Long id;
    private String code;
    private String enOption;
    private String frOption;
    private String knOption;
    private boolean isGreen;
    private String description;

    public ResponseOptionsResponseDTO(Long id, String code, String enOption, String frOption, String knOption, boolean isGreen, String description) {
        this.id = id;
        this.code = code;
        this.enOption = enOption;
        this.frOption = frOption;
        this.knOption = knOption;
        this.isGreen = isGreen;
        this.description = description;
    }
}
