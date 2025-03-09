package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.AssessmentQuestionsDTO;
import rw.dsacco.clat.dto.AssessmentQuestionsResponseDTO;
import rw.dsacco.clat.models.AssessmentQuestions;
import rw.dsacco.clat.services.AssessmentQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assessment-questions")
public class AssessmentQuestionsController {

    @Autowired
    private AssessmentQuestionsService questionsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AssessmentQuestionsResponseDTO>> createQuestion(
            @RequestBody AssessmentQuestionsDTO questionsDTO) {

        try {
            AssessmentQuestions question = new AssessmentQuestions();
            question.setEnQuestion(questionsDTO.getEnQuestion());
            question.setFrQuestion(questionsDTO.getFrQuestion());
            question.setKnQuestion(questionsDTO.getKnQuestion());
            question.setDescription(questionsDTO.getDescription());
            question.setMultipleChoice(questionsDTO.isMultipleChoice());

            AssessmentQuestions savedQuestion = questionsService.createQuestion(question, questionsDTO.getActivityCode());

            return ResponseEntity.ok(ApiResponse.success("Assessment question created successfully", convertToDTO(savedQuestion)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create question: " + e.getMessage()));
        }
    }


    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<AssessmentQuestionsResponseDTO>>> getAllQuestions() {
        List<AssessmentQuestionsResponseDTO> questions = questionsService.getAllQuestions()
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Assessment questions retrieved successfully", questions));
    }

    @GetMapping("/activity/{activityCode}")
    public ResponseEntity<ApiResponse<List<AssessmentQuestionsResponseDTO>>> getQuestionsByActivity(
            @PathVariable String activityCode) {
        List<AssessmentQuestionsResponseDTO> questions = questionsService.getQuestionsByActivityCode(activityCode)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Assessment questions retrieved successfully", questions));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<AssessmentQuestionsResponseDTO>> getQuestionByCode(@PathVariable String code) {
        Optional<AssessmentQuestions> questionOptional = questionsService.getQuestionByCode(code);

        if (questionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Question not found"));
        }

        return ResponseEntity.ok(ApiResponse.success("Assessment question retrieved successfully", convertToDTO(questionOptional.get())));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AssessmentQuestionsResponseDTO>>> searchQuestions(
            @RequestParam String keyword) {
        List<AssessmentQuestionsResponseDTO> questions = questionsService.searchQuestions(keyword)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", questions));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<AssessmentQuestionsResponseDTO>> updateQuestion(
            @PathVariable String code, @RequestBody AssessmentQuestionsDTO questionsDTO) {

        Optional<AssessmentQuestions> questionOptional = questionsService.getQuestionByCode(code);

        if (questionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Question with code " + code + " not found."));
        }

        AssessmentQuestions question = questionOptional.get();
        question.setEnQuestion(questionsDTO.getEnQuestion());
        question.setFrQuestion(questionsDTO.getFrQuestion());
        question.setKnQuestion(questionsDTO.getKnQuestion());
        question.setDescription(questionsDTO.getDescription());
        question.setMultipleChoice(questionsDTO.isMultipleChoice());

        AssessmentQuestions updatedQuestion = questionsService.updateQuestion(question);

        return ResponseEntity.ok(ApiResponse.success("Assessment question updated successfully", convertToDTO(updatedQuestion)));
    }


    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable String code) {
        Optional<AssessmentQuestions> questionOptional = questionsService.getQuestionByCode(code);

        if (questionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Question not found"));
        }

        questionsService.deleteQuestion(questionOptional.get().getId());
        return ResponseEntity.ok(ApiResponse.success("Assessment question deleted successfully", null));
    }

    private AssessmentQuestionsResponseDTO convertToDTO(AssessmentQuestions question) {
        return new AssessmentQuestionsResponseDTO(
                question.getId(),
                question.getCode(),
                question.getEnQuestion(),
                question.getFrQuestion(),
                question.getKnQuestion(),
                question.getDescription(),
                question.isMultipleChoice()
        );
    }

}
