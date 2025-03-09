package rw.dsacco.clat.services;

import rw.dsacco.clat.models.Activities;
import rw.dsacco.clat.models.Stages;
import rw.dsacco.clat.repositories.ActivitiesRepository;
import rw.dsacco.clat.repositories.StagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivitiesService {

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @Autowired
    private StagesRepository stagesRepository;

    public Activities createActivity(Activities activity, Long stageId) {
        Optional<Stages> stageOptional = stagesRepository.findById(stageId);
        if (stageOptional.isEmpty()) {
            throw new RuntimeException("Stage not found");
        }
        activity.setStage(stageOptional.get());
        return activitiesRepository.save(activity);
    }

    public List<Activities> getAllActivities() {
        return activitiesRepository.findAll();
    }

    public List<Activities> getActivitiesByStageId(Long stageId) {
        return activitiesRepository.findByStageId(stageId);
    }

    public Optional<Activities> getActivityByCode(String code) {
        return activitiesRepository.findByCode(code);
    }

    public List<Activities> searchActivities(String keyword) {
        return activitiesRepository.findByEnActivityContainingIgnoreCase(keyword);
    }

    public Activities updateActivity(Activities activity) {
        return activitiesRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        activitiesRepository.deleteById(id);
    }
}
