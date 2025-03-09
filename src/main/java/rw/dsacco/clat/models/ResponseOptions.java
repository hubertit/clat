package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "response_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestions question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enOption;

    @Column(columnDefinition = "TEXT")
    private String frOption;

    @Column(columnDefinition = "TEXT")
    private String knOption;

    @Builder.Default // ✅ Fixes @Builder ignoring the default value
    @Column(nullable = false)
    private boolean isGreen = false;

    @Column(columnDefinition = "TEXT")
    private String description;

    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "O-" + (100000 + new Random().nextInt(900000)); // Generates O-XXXXXX
        }
    }
}
