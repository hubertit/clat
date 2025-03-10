package rw.dsacco.clat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.dsacco.clat.models.Response;

import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    // Find response by assessmentId, questionId, and optionId
    Optional<Response> findByAssessmentIdAndQuestionIdAndOptionId(Long assessmentId, Long questionId, Long optionId);
}
