package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Optional<Assessment> findByCode(UUID code);
    void deleteByCode(UUID code);
}
