package rw.dsacco.clat.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(nullable = false, unique = true, updatable = false)
    private UUID code = UUID.randomUUID();

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, unique = true)
    private String loanApplicationNo;

    @Column(nullable = false)
    private BigDecimal loanApplicationAmount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private Long saccoId;

    @Column(nullable = true)
    private Double progress;

    @Column(nullable = true)
    private BigDecimal totalCost;

    @Column(nullable = true)
    private BigDecimal greenCost;

    @Column(nullable = true)
    private BigDecimal nonGreenCost;

    @Column(nullable = true)
    private String customerName; // ✅ New

    @Column(nullable = true)
    private String customerGender; // ✅ New

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
