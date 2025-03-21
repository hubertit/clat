package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.StagesDTO;
import rw.dsacco.clat.models.Stages;
import rw.dsacco.clat.services.StagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
@RestController
@RequestMapping("/api/stages")
public class StagesController {

    @Autowired
    private StagesService stagesService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Stages>> createStage(@RequestBody StagesDTO stagesDTO) {
        Stages stage = new Stages();
        stage.setEnStage(stagesDTO.getEnStage());
        stage.setFrStage(stagesDTO.getFrStage());
        stage.setKnStage(stagesDTO.getKnStage());
        stage.setDescription(stagesDTO.getDescription());

        Stages savedStage = stagesService.createStage(stage, stagesDTO.getProductId());
        return ResponseEntity.ok(ApiResponse.success("Stage created successfully", savedStage));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Stages>>> getAllStages() {
        List<Stages> stages = stagesService.getAllStages();
        return ResponseEntity.ok(ApiResponse.success("Stages retrieved successfully", stages));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<Stages>> getStageByCode(@PathVariable String code) {
        Optional<Stages> stageOptional = stagesService.getStageByCode(code);

        if (stageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Stage not found"));
        }

        return ResponseEntity.ok(ApiResponse.success("Stage retrieved successfully", stageOptional.get()));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<Stages>>> getStagesByProduct(@PathVariable Long productId) {
        List<Stages> stages = stagesService.getStagesByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Stages retrieved successfully", stages));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Stages>> updateStage(@PathVariable String code, @RequestBody StagesDTO stagesDTO) {
        Optional<Stages> stageOptional = stagesService.getStageByCode(code);

        if (stageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Stage not found"));
        }

        Stages stage = stageOptional.get();
        stage.setEnStage(stagesDTO.getEnStage());
        stage.setFrStage(stagesDTO.getFrStage());
        stage.setKnStage(stagesDTO.getKnStage());
        stage.setDescription(stagesDTO.getDescription());

        Stages updatedStage = stagesService.updateStage(stage);
        return ResponseEntity.ok(ApiResponse.success("Stage updated successfully", updatedStage));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteStage(@PathVariable String code) {
        Optional<Stages> stageOptional = stagesService.getStageByCode(code);

        if (stageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Stage not found"));
        }

        stagesService.deleteStage(stageOptional.get().getId());
        return ResponseEntity.ok(ApiResponse.success("Stage deleted successfully", null));
    }
}
