package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.ActivitiesDTO;
import rw.dsacco.clat.models.Activities;
import rw.dsacco.clat.services.ActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activities")
public class ActivitiesController {

    @Autowired
    private ActivitiesService activitiesService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Activities>> createActivity(@RequestBody ActivitiesDTO activitiesDTO) {
        Activities activity = new Activities();
        activity.setEnActivity(activitiesDTO.getEnActivity());
        activity.setFrActivity(activitiesDTO.getFrActivity());
        activity.setKnActivity(activitiesDTO.getKnActivity());
        activity.setDescription(activitiesDTO.getDescription());

        Activities savedActivity = activitiesService.createActivity(activity, activitiesDTO.getStageId());
        return ResponseEntity.ok(ApiResponse.success("Activity created successfully", savedActivity));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Activities>>> getAllActivities() {
        List<Activities> activities = activitiesService.getAllActivities();
        return ResponseEntity.ok(ApiResponse.success("Activities retrieved successfully", activities));
    }

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<ApiResponse<List<Activities>>> getActivitiesByStage(@PathVariable Long stageId) {
        List<Activities> activities = activitiesService.getActivitiesByStageId(stageId);
        return ResponseEntity.ok(ApiResponse.success("Activities retrieved successfully", activities));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<Activities>> getActivityByCode(@PathVariable String code) {
        Optional<Activities> activityOptional = activitiesService.getActivityByCode(code);

        if (activityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Activity not found"));
        }

        return ResponseEntity.ok(ApiResponse.success("Activity retrieved successfully", activityOptional.get()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Activities>>> searchActivities(@RequestParam String keyword) {
        List<Activities> activities = activitiesService.searchActivities(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", activities));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Activities>> updateActivity(@PathVariable String code, @RequestBody ActivitiesDTO activitiesDTO) {
        Optional<Activities> activityOptional = activitiesService.getActivityByCode(code);

        if (activityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Activity not found"));
        }

        Activities activity = activityOptional.get();
        activity.setEnActivity(activitiesDTO.getEnActivity());
        activity.setFrActivity(activitiesDTO.getFrActivity());
        activity.setKnActivity(activitiesDTO.getKnActivity());
        activity.setDescription(activitiesDTO.getDescription());

        Activities updatedActivity = activitiesService.updateActivity(activity);
        return ResponseEntity.ok(ApiResponse.success("Activity updated successfully", updatedActivity));
    }


    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(@PathVariable String code) {
        Optional<Activities> activityOptional = activitiesService.getActivityByCode(code);

        if (activityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Activity not found"));
        }

        activitiesService.deleteActivity(activityOptional.get().getId());
        return ResponseEntity.ok(ApiResponse.success("Activity deleted successfully", null));
    }
}
