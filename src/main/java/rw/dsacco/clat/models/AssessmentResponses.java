package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "assessment_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResponses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(name = "is_green", nullable = false)
    private Boolean isGreen;
}
