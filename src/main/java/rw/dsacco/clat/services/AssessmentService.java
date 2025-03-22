package rw.dsacco.clat.services;

import rw.dsacco.clat.dto.*;
import rw.dsacco.clat.models.*;
import rw.dsacco.clat.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AssessmentResponsesRepository assessmentResponsesRepository;

    @Autowired
    private AssessmentQuestionsRepository assessmentQuestionsRepository;

    public ApiResponse<AssessmentResponseDTO> createOrUpdateAssessment(AssessmentDTO dto) {
        if (dto.getProductId() == null) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Missing required field: productId");
        }
        if (dto.getLoanApplicationNo() == null || dto.getLoanApplicationNo().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Missing required field: loanApplicationNo");
        }
        if (dto.getLoanApplicationAmount() == null) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Missing required field: loanApplicationAmount");
        }
        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Missing required field: status");
        }

        Optional<Assessment> existingAssessment = assessmentRepository.findByLoanApplicationNo(dto.getLoanApplicationNo());

        Assessment assessment;
        boolean isUpdate = false;

        if (existingAssessment.isPresent()) {
            assessment = existingAssessment.get();
            isUpdate = true;
        } else {
            assessment = new Assessment();
            assessment.setCode(UUID.randomUUID());
            assessment.setCreatedAt(LocalDateTime.now());
        }

        assessment.setProductId(dto.getProductId());
        assessment.setLoanApplicationAmount(dto.getLoanApplicationAmount());
        assessment.setStatus(dto.getStatus());
        assessment.setLoanApplicationNo(dto.getLoanApplicationNo());
        assessment.setSaccoId(dto.getSaccoId());
        assessment.setProgress(dto.getProgress());
        assessment.setTotalCost(dto.getTotalCost());
        assessment.setGreenCost(dto.getGreenCost());
        assessment.setNonGreenCost(dto.getNonGreenCost());
        assessment.setCustomerName(dto.getCustomerName());
        assessment.setCustomerGender(dto.getCustomerGender());

        assessmentRepository.save(assessment);
        return ApiResponse.success(isUpdate ? "Assessment updated successfully" : "Assessment created successfully", convertToDTO(assessment));
    }

    public ApiResponse<List<AssessmentResponseDTO>> getAllAssessments() {
        List<AssessmentResponseDTO> assessments = assessmentRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        if (assessments.isEmpty()) {
            throw new NoSuchElementException("No assessments found.");
        }
        return ApiResponse.success("Fetched all assessments", assessments);
    }

    public ApiResponse<AssessmentResponseDTO> getAssessmentByCode(UUID code) {
        return assessmentRepository.findByCode(code)
                .map(a -> ApiResponse.success("Assessment found", convertToDTO(a)))
                .orElseThrow(() -> new NoSuchElementException("Assessment not found with code: " + code));
    }

    @Transactional
    public ApiResponse<String> deleteAssessment(UUID code) {
        if (!assessmentRepository.existsByCode(code)) {
            throw new NoSuchElementException("Assessment not found with code: " + code);
        }
        assessmentRepository.deleteByCode(code);
        return ApiResponse.success("Assessment deleted successfully", null);
    }

    public ApiResponse<List<AssessmentResponseDTO>> searchAssessments(String keyword) {
        List<AssessmentResponseDTO> results = assessmentRepository.findAll().stream()
                .filter(a -> a.getLoanApplicationNo().contains(keyword))
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            throw new NoSuchElementException("No assessments found matching keyword: " + keyword);
        }
        return ApiResponse.success("Search results", results);
    }

    public ApiResponse<AssessmentResponseDTO> approveAssessment(UUID code) {
        return updateStatus(code, "APPROVED");
    }

    public ApiResponse<AssessmentResponseDTO> rejectAssessment(UUID code) {
        return updateStatus(code, "REJECTED");
    }

    public ApiResponse<AssessmentResponseDTO> markProcessing(UUID code) {
        return updateStatus(code, "PROCESSING");
    }

    public ApiResponse<AssessmentResponseDTO> submitAssessment(UUID code) {
        return updateStatus(code, "SUBMITTED");
    }

    private ApiResponse<AssessmentResponseDTO> updateStatus(UUID code, String status) {
        Optional<Assessment> assessment = assessmentRepository.findByCode(code);
        if (assessment.isEmpty()) {
            throw new NoSuchElementException("Assessment not found with code: " + code);
        }
        Assessment a = assessment.get();
        a.setStatus(status);
        assessmentRepository.save(a);
        return ApiResponse.success("Assessment updated to " + status, convertToDTO(a));
    }

    public ApiResponse<List<AssessmentResponseDTO>> filterAssessments(
            Long productId, String status, String loanApplicationNo,
            BigDecimal minLoanAmount, BigDecimal maxLoanAmount,
            String startDate, String endDate, String sortBy, String sortOrder) {

        List<AssessmentResponseDTO> assessments = assessmentRepository.findAll()
                .stream()
                .filter(a -> (productId == null || a.getProductId().equals(productId)))
                .filter(a -> (status == null || a.getStatus().equalsIgnoreCase(status)))
                .filter(a -> (loanApplicationNo == null || a.getLoanApplicationNo().equalsIgnoreCase(loanApplicationNo)))
                .filter(a -> (minLoanAmount == null || a.getLoanApplicationAmount().compareTo(minLoanAmount) >= 0))
                .filter(a -> (maxLoanAmount == null || a.getLoanApplicationAmount().compareTo(maxLoanAmount) <= 0))
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        if (assessments.isEmpty()) {
            throw new NoSuchElementException("No assessments found for the given filters.");
        }

        return ApiResponse.success("Filtered assessments retrieved successfully", assessments);
    }

    private AssessmentResponseDTO convertToDTO(Assessment assessment) {
        String productName = productRepository.findById(assessment.getProductId())
                .map(p -> p.getEnProduct())
                .orElse("Unknown Product");

        AssessmentResponseDTO dto = new AssessmentResponseDTO();
        dto.setId(assessment.getId());
        dto.setSaccoId(assessment.getSaccoId());
        dto.setCode(assessment.getCode());
        dto.setProductName(productName);
        dto.setLoanApplicationNo(assessment.getLoanApplicationNo());
        dto.setLoanApplicationAmount(assessment.getLoanApplicationAmount());
        dto.setCustomerName(assessment.getCustomerName());
        dto.setCustomerGender(assessment.getCustomerGender());
        dto.setProgress(assessment.getProgress());
        dto.setTotalCost(assessment.getTotalCost());
        dto.setGreenCost(assessment.getGreenCost());
        dto.setNonGreenCost(assessment.getNonGreenCost());
        dto.setStatus(assessment.getStatus());
        dto.setCreatedAt(assessment.getCreatedAt());

        dto.setQuestions(fetchAnsweredQuestions(assessment.getId()));
        return dto;
    }

    private List<AnsweredQuestionDTO> fetchAnsweredQuestions(Long assessmentId) {
        return assessmentResponsesRepository.findByAssessmentId(assessmentId).stream()
                .map(response -> {
                    AssessmentQuestions question = assessmentQuestionsRepository
                            .findById(response.getQuestionId())
                            .orElse(null);
                    if (question == null) return null;

                    AnsweredQuestionDTO dto = new AnsweredQuestionDTO();
                    dto.setQuestionCode(question.getCode());
                    dto.setEnQuestion(question.getEnQuestion());
                    dto.setFrQuestion(question.getFrQuestion());
                    dto.setKnQuestion(question.getKnQuestion());
                    dto.setDescription(question.getDescription());
                    dto.setOptionId(response.getOptionId());
                    dto.setCost(response.getCost());
                    dto.setIsGreen(response.getIsGreen());
                    return dto;
                })
                .filter(q -> q != null)
                .collect(Collectors.toList());
    }
}
