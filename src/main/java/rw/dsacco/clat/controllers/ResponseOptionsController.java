package rw.dsacco.clat.controllers;

import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.ResponseOptionsDTO;
import rw.dsacco.clat.dto.ResponseOptionsResponseDTO;
import rw.dsacco.clat.models.ResponseOptions;
import rw.dsacco.clat.services.ResponseOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
@RestController
@RequestMapping("/api/response-options")
public class ResponseOptionsController {

    @Autowired
    private ResponseOptionsService optionsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ResponseOptionsResponseDTO>> createOption(@RequestBody ResponseOptionsDTO optionsDTO) {
        try {
            ResponseOptions option = new ResponseOptions();
            option.setEnOption(optionsDTO.getEnOption());
            option.setFrOption(optionsDTO.getFrOption());
            option.setKnOption(optionsDTO.getKnOption());
            option.setGreen(optionsDTO.isGreen());
            option.setDescription(optionsDTO.getDescription());

            ResponseOptions savedOption = optionsService.createOption(option, optionsDTO.getQuestionCode());

            return ResponseEntity.ok(ApiResponse.success("Response option created successfully", convertToDTO(savedOption)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/question/{questionCode}")
    public ResponseEntity<ApiResponse<List<ResponseOptionsResponseDTO>>> getOptionsByQuestion(@PathVariable String questionCode) {
        List<ResponseOptionsResponseDTO> options = optionsService.getOptionsByQuestionCode(questionCode)
                .stream().map(this::convertToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Response options retrieved successfully", options));
    }

    private ResponseOptionsResponseDTO convertToDTO(ResponseOptions option) {
        return new ResponseOptionsResponseDTO(option.getId(), option.getCode(), option.getEnOption(), option.getFrOption(), option.getKnOption(), option.isGreen(), option.getDescription());
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<ResponseOptionsResponseDTO>> getOptionByCode(@PathVariable String code) {
        Optional<ResponseOptions> optionOptional = optionsService.getOptionByCode(code);

        if (optionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Response option with code " + code + " not found in the database."));
        }

        return ResponseEntity.ok(ApiResponse.success("Response option retrieved successfully", convertToDTO(optionOptional.get())));
    }



    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteOption(@PathVariable String code) {
        Optional<ResponseOptions> optionOptional = optionsService.getOptionByCode(code);

        if (optionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Response option with code " + code + " not found."));
        }

        optionsService.deleteOption(optionOptional.get().getId());
        return ResponseEntity.ok(ApiResponse.success("Response option deleted successfully", null));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<ResponseOptionsResponseDTO>> updateOption(
            @PathVariable String code, @RequestBody ResponseOptionsDTO optionsDTO) {

        Optional<ResponseOptions> optionOptional = optionsService.getOptionByCode(code);

        if (optionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Response option with code " + code + " not found."));
        }

        ResponseOptions option = optionOptional.get();
        option.setEnOption(optionsDTO.getEnOption());
        option.setFrOption(optionsDTO.getFrOption());
        option.setKnOption(optionsDTO.getKnOption());
        option.setGreen(optionsDTO.isGreen());
        option.setDescription(optionsDTO.getDescription());

        ResponseOptions updatedOption = optionsService.updateOption(option);

        return ResponseEntity.ok(ApiResponse.success("Response option updated successfully", convertToDTO(updatedOption)));
    }


}
