package rw.dsacco.clat.repositories;

import rw.dsacco.clat.models.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ActivitiesRepository extends JpaRepository<Activities, Long> {
    Optional<Activities> findByCode(String code);
    List<Activities> findByEnActivityContainingIgnoreCase(String keyword);
    List<Activities> findByStageId(Long stageId); // ✅ Get activities by stage
}
