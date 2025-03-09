package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "stages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @Column(nullable = false)
    private String enStage;

    @Column
    private String frStage;

    @Column
    private String knStage;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Auto-generate code before saving
    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "S-" + (100000 + new Random().nextInt(900000)); // Generates S-XXXXXX
        }
    }
}
