package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.AssessmentResponses;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssessmentResponsesRepository extends JpaRepository<AssessmentResponses, Long> {
    List<AssessmentResponses> findByAssessmentId(Long assessmentId);
}
