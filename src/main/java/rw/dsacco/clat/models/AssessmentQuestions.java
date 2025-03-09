package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "assessment_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activities activity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enQuestion;

    @Column(columnDefinition = "TEXT")
    private String frQuestion;

    @Column(columnDefinition = "TEXT")
    private String knQuestion;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Auto-generate code before saving
    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "Q-" + (100000 + new Random().nextInt(900000)); // Generates Q-XXXXXX
        }
    }
}
