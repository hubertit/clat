package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.*;
import rw.dsacco.clat.models.Assessment;
import rw.dsacco.clat.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> createAssessment(@RequestBody AssessmentDTO dto) {
        try {
            Assessment assessment = assessmentService.createAssessment(dto);
            return ResponseEntity.ok(ApiResponse.success("Assessment created successfully", assessmentService.convertToDTO(assessment)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> getAssessment(@PathVariable UUID code) {
        Optional<AssessmentResponseDTO> assessment = assessmentService.getAssessmentByCode(code);
        return assessment.map(a -> ResponseEntity.ok(ApiResponse.success("Assessment found", a)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Assessment not found")));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteAssessment(@PathVariable UUID code) {
        assessmentService.deleteAssessment(code);
        return ResponseEntity.ok(ApiResponse.success("Assessment deleted successfully", null));
    }


    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> updateAssessment(
            @PathVariable UUID code, @RequestBody AssessmentDTO dto) {

        Optional<Assessment> existingAssessment = assessmentService.getAssessmentByUUID(code);
        if (existingAssessment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Assessment not found"));
        }

        Assessment updatedAssessment = assessmentService.updateAssessment(existingAssessment.get(), dto);
        return ResponseEntity.ok(ApiResponse.success("Assessment updated successfully",
                assessmentService.convertToDTO(updatedAssessment)));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<AssessmentResponseDTO>>> getAllAssessments() {
        List<AssessmentResponseDTO> assessments = assessmentService.getAllAssessments()
                .stream().map(assessmentService::convertToDTO).toList();

        return ResponseEntity.ok(ApiResponse.success("Assessments retrieved successfully", assessments));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<AssessmentResponseDTO>>> filterAssessments(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String loanApplicationNo,
            @RequestParam(required = false) BigDecimal minLoanAmount,
            @RequestParam(required = false) BigDecimal maxLoanAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<AssessmentResponseDTO> assessments = assessmentService.filterAssessments(
                        customerId, productId, status, loanApplicationNo, minLoanAmount, maxLoanAmount,
                        startDate, endDate, sortBy, sortOrder)
                .stream().map(assessmentService::convertToDTO).toList();

        return ResponseEntity.ok(ApiResponse.success("Filtered assessments retrieved successfully", assessments));
    }



}
