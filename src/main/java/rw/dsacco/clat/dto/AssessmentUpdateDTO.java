package rw.dsacco.clat.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AssessmentUpdateDTO {
    private Integer approvedBy; // ✅ Ensure it matches the database column

    // Getter
    public Integer getApprovedBy() {
        return approvedBy;
    }

    // Setter
    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }
}