package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // ✅ Ensure this field exists

    @Column(nullable = false)
    private String enProduct;

    @Column
    private String frProduct;

    @Column
    private String knProduct;

    @Column(columnDefinition = "TEXT")
    private String description;
}
