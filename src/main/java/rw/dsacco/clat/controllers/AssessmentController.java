package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.*;
import rw.dsacco.clat.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> createAssessment(@RequestBody AssessmentDTO dto) {
        return ResponseEntity.ok(assessmentService.createOrUpdateAssessment(dto));
    }

    @PutMapping("/{code}") // ✅ PUT for updating assessments
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> updateAssessment(
            @PathVariable UUID code, @RequestBody AssessmentDTO dto) {
        return ResponseEntity.ok(assessmentService.createOrUpdateAssessment(dto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> getAssessment(@PathVariable UUID code) {
        return ResponseEntity.ok(assessmentService.getAssessmentByCode(code));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<String>> deleteAssessment(@PathVariable UUID code) {
        return ResponseEntity.ok(assessmentService.deleteAssessment(code));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<AssessmentResponseDTO>>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AssessmentResponseDTO>>> searchAssessments(@RequestParam String keyword) {
        return ResponseEntity.ok(assessmentService.searchAssessments(keyword));
    }

    // ✅ Status Update APIs
    @PutMapping("/{code}/approve")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> approveAssessment(@PathVariable UUID code) {
        return ResponseEntity.ok(assessmentService.approveAssessment(code));
    }

    @PutMapping("/{code}/reject")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> rejectAssessment(@PathVariable UUID code) {
        return ResponseEntity.ok(assessmentService.rejectAssessment(code));
    }

    @PutMapping("/{code}/processing")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> markProcessing(@PathVariable UUID code) {
        return ResponseEntity.ok(assessmentService.markProcessing(code));
    }

    @PutMapping("/{code}/submit")
    public ResponseEntity<ApiResponse<AssessmentResponseDTO>> submitAssessment(@PathVariable UUID code) {
        return ResponseEntity.ok(assessmentService.submitAssessment(code));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<AssessmentResponseDTO>>> filterAssessments(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String loanApplicationNo,
            @RequestParam(required = false) BigDecimal minLoanAmount,
            @RequestParam(required = false) BigDecimal maxLoanAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        return ResponseEntity.ok(assessmentService.filterAssessments(
                productId, status, loanApplicationNo, minLoanAmount, maxLoanAmount, startDate, endDate, sortBy, sortOrder));
    }
}
