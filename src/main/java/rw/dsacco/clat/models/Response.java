package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "assessment_responses") // ✅ Renamed table to "responses"
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestions question;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private ResponseOptions option;

    @Column(nullable = false)
    private Double cost;

    @Column(name = "is_green", nullable = false)
    private Boolean isGreen;
}
