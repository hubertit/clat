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

    @Column(name = "product_id") // ✅ No longer a foreign key
    private Long productId; // ✅ Changed from Product object to Long

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @Column(nullable = false, unique = true)
    private String loanApplicationNo;

    @Column(nullable = false)
    private BigDecimal loanApplicationAmount;

    @ManyToOne
    @JoinColumn(name = "done_by")
    private User doneBy;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Builder.Default
    @Column(nullable = false)
    private String status = "PENDING";

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;
}
