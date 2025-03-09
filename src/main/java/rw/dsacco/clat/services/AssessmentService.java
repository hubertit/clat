package rw.dsacco.clat.services;

import rw.dsacco.clat.dto.AssessmentDTO;
import rw.dsacco.clat.dto.AssessmentResponseDTO;
import rw.dsacco.clat.models.*;
import rw.dsacco.clat.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.math.BigDecimal;



@Service
public class AssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentService.class);

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository; // ✅ Ensure this is present


    public Assessment createAssessment(AssessmentDTO dto) {
        logger.info("Received Assessment Request: {}", dto);

        Optional<User> customer = userRepository.findById(dto.getCustomerId());

        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer ID not found: " + dto.getCustomerId());
        }

        logger.info("Product ID: {}", dto.getProductId()); // ✅ Debugging log

        Assessment assessment = Assessment.builder()
                .code(UUID.randomUUID())
                .customer(customer.get())
                .productId(dto.getProductId()) // ✅ Directly storing productId
                .loanApplicationNo(dto.getLoanApplicationNo())
                .loanApplicationAmount(dto.getLoanApplicationAmount())
                .doneBy(null)
                .status("PENDING")
                .build();

        try {
            Assessment savedAssessment = assessmentRepository.save(assessment);
            logger.info("Assessment created successfully: {}", savedAssessment);
            return savedAssessment;
        } catch (Exception e) {
            logger.error("Error saving assessment: ", e);
            throw new RuntimeException("Failed to create assessment: " + e.getMessage());
        }
    }

    public Optional<AssessmentResponseDTO> getAssessmentByCode(UUID code) {
        return assessmentRepository.findByCode(code).map(this::convertToDTO);
    }

    public void deleteAssessment(UUID code) {
        assessmentRepository.deleteByCode(code);
    }

    public AssessmentResponseDTO convertToDTO(Assessment assessment) {
        // Fetch product name using `productId`
        String productName = null;
        if (assessment.getProductId() != null) {
            Optional<Product> product = productRepository.findById(assessment.getProductId());
            productName = product.map(Product::getEnProduct).orElse("Unknown Product");
        }

        // Fetch doneBy name
        String doneByName = (assessment.getDoneBy() != null) ? assessment.getDoneBy().getName() : null;

        return new AssessmentResponseDTO(
                assessment.getCode(),
                assessment.getCustomer().getName(),
                productName,
                assessment.getLoanApplicationNo(),
                assessment.getLoanApplicationAmount(),
                doneByName,
                (assessment.getApprovedBy() != null) ? assessment.getApprovedBy().getName() : null,
                assessment.getStatus(),
                assessment.getCreatedAt(),
                assessment.getUpdatedAt()
        );
    }

    public Optional<Assessment> getAssessmentByUUID(UUID code) {
        return assessmentRepository.findByCode(code);
    }


    public Assessment updateAssessment(Assessment assessment, AssessmentDTO dto) {
        if (dto.getLoanApplicationAmount() != null) {
            assessment.setLoanApplicationAmount(dto.getLoanApplicationAmount());
        }
        if (dto.getStatus() != null) {
            assessment.setStatus(dto.getStatus());
        }

        assessment.setUpdatedAt(LocalDateTime.now());

        return assessmentRepository.save(assessment);
    }

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public List<Assessment> filterAssessments(
            Long customerId, Long productId, String status, String loanApplicationNo,
            BigDecimal minLoanAmount, BigDecimal maxLoanAmount,
            String startDate, String endDate, String sortBy, String sortOrder) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = (startDate != null) ? LocalDateTime.parse(startDate, formatter) : null;
        LocalDateTime endDateTime = (endDate != null) ? LocalDateTime.parse(endDate, formatter) : null;

        List<Assessment> filteredAssessments = assessmentRepository.findAll().stream()
                .filter(a -> (customerId == null || a.getCustomer().getId().equals(customerId)))
                .filter(a -> (productId == null || a.getProductId().equals(productId)))
                .filter(a -> (status == null || a.getStatus().equalsIgnoreCase(status)))
                .filter(a -> (loanApplicationNo == null || a.getLoanApplicationNo().equalsIgnoreCase(loanApplicationNo)))
                .filter(a -> (minLoanAmount == null || a.getLoanApplicationAmount().compareTo(minLoanAmount) >= 0))
                .filter(a -> (maxLoanAmount == null || a.getLoanApplicationAmount().compareTo(maxLoanAmount) <= 0))
                .filter(a -> (startDateTime == null || a.getCreatedAt().isAfter(startDateTime)))
                .filter(a -> (endDateTime == null || a.getCreatedAt().isBefore(endDateTime)))
                .toList();

        // Sorting logic
        Comparator<Assessment> comparator = switch (sortBy.toLowerCase()) {
            case "updatedat" -> Comparator.comparing(Assessment::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "loanapplicationamount" -> Comparator.comparing(Assessment::getLoanApplicationAmount);
            case "status" -> Comparator.comparing(Assessment::getStatus);
            default -> Comparator.comparing(Assessment::getCreatedAt);
        };

        // Apply sorting order
        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return filteredAssessments.stream().sorted(comparator).toList();
    }

}
