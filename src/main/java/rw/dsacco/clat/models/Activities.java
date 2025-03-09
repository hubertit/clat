package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "stage_id", nullable = false)
    private Stages stage;

    @Column(nullable = false)
    private String enActivity;

    @Column
    private String frActivity;

    @Column
    private String knActivity;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Auto-generate code before saving
    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "A-" + (100000 + new Random().nextInt(900000)); // Generates A-XXXXXX
        }
    }
}
