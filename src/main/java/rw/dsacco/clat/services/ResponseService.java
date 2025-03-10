package rw.dsacco.clat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.ResponseDTO;
import rw.dsacco.clat.models.*;
import rw.dsacco.clat.repositories.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentQuestionsRepository questionRepository;

    @Autowired
    private ResponseOptionsRepository optionRepository;

    // Create or Update Response
    public ApiResponse<ResponseDTO> createOrUpdateResponse(ResponseDTO dto) {
        Optional<Assessment> assessmentOpt = assessmentRepository.findById(dto.getAssessmentId());
        if (assessmentOpt.isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND, "Assessment not found");
        }

        Optional<AssessmentQuestions> questionOpt = questionRepository.findById(dto.getQuestionId());
        if (questionOpt.isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND, "Question not found");
        }

        Optional<ResponseOptions> optionOpt = optionRepository.findById(dto.getOptionId());
        if (optionOpt.isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND, "Option not found");
        }

        Assessment assessment = assessmentOpt.get();
        AssessmentQuestions question = questionOpt.get();
        ResponseOptions option = optionOpt.get();

        // Check if the response already exists (Same assessmentId, questionId, optionId)
        Optional<Response> existingResponse = responseRepository.findByAssessmentIdAndQuestionIdAndOptionId(
                dto.getAssessmentId(), dto.getQuestionId(), dto.getOptionId()
        );

        Response response;
        if (existingResponse.isPresent()) {
            // Update existing response
            response = existingResponse.get();
            response.setCost(dto.getCost());
            response.setIsGreen(dto.getIsGreen());
        } else {
            // Create new response
            response = new Response();
            response.setAssessment(assessment);
            response.setQuestion(question);
            response.setOption(option);
            response.setCost(dto.getCost());
            response.setIsGreen(dto.getIsGreen());
        }

        responseRepository.save(response);
        return ApiResponse.success("Response saved successfully", mapToDTO(response));
    }

    // Get All Responses
    public ApiResponse<List<ResponseDTO>> getAllResponses() {
        List<ResponseDTO> responses = responseRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
        return ApiResponse.success("Fetched all responses", responses);
    }

    // Get Response By ID
    public ApiResponse<ResponseDTO> getResponseById(Long id) {
        Optional<Response> responseOpt = responseRepository.findById(id);
        if (responseOpt.isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND, "Response not found");
        }
        return ApiResponse.success("Response found", mapToDTO(responseOpt.get()));
    }

    // Update Response
    public ApiResponse<ResponseDTO> updateResponse(Long id, ResponseDTO dto) {
        Optional<Response> responseOpt = responseRepository.findById(id);
        if (responseOpt.isEmpty()) {
            return ApiResponse.error(HttpStatus.NOT_FOUND, "Response not found");
        }

        Response response = responseOpt.get();
        response.setCost(dto.getCost());
        response.setIsGreen(dto.getIsGreen());
        responseRepository.save(response);

        return ApiResponse.success("Response updated successfully", mapToDTO(response));
    }

    // Delete Response
    public ApiResponse<String> deleteResponse(Long id) {
        if (!responseRepository.existsById(id)) {
            return ApiResponse.error(HttpStatus.NOT_FOUND, "Response not found");
        }
        responseRepository.deleteById(id);
        return ApiResponse.success("Response deleted successfully", null);
    }

    // Map Model to DTO
    private ResponseDTO mapToDTO(Response response) {
        ResponseDTO dto = new ResponseDTO();
        dto.setId(response.getId());
        dto.setAssessmentId(response.getAssessment().getId());
        dto.setQuestionId(response.getQuestion().getId());
        dto.setOptionId(response.getOption().getId());
        dto.setCost(response.getCost());
        dto.setIsGreen(response.getIsGreen());
        return dto;
    }
}
