package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Optional<Assessment> findByCode(UUID code);
    Optional<Assessment> findByLoanApplicationNo(String loanApplicationNo);
    boolean existsByCode(UUID code);
    void deleteByCode(UUID code);
    List<Assessment> findAllByOrderByIdDesc();
}
