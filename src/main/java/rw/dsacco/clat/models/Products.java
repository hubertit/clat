package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @Column(nullable = false)
    private String enProduct;

    @Column
    private String frProduct;

    @Column
    private String knProduct;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Generate unique code before saving
    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = "P-" + new Random().nextInt(900000) + 100000; // Generates PXXXXXX (6 random numbers)
        }
    }
}
