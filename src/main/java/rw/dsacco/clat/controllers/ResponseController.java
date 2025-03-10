package rw.dsacco.clat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.dsacco.clat.dto.ApiResponse;
import rw.dsacco.clat.dto.ResponseDTO;
import rw.dsacco.clat.services.ResponseService;

import java.util.List;

@RestController
@RequestMapping("/api/responses") // ✅ New API path
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    // Create or Update Response
    @PostMapping
    public ResponseEntity<ApiResponse<ResponseDTO>> createOrUpdateResponse(@RequestBody ResponseDTO dto) {
        return ResponseEntity.ok(responseService.createOrUpdateResponse(dto));
    }

    // Get All Responses
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseDTO>>> getAllResponses() {
        return ResponseEntity.ok(responseService.getAllResponses());
    }

    // Get Response By ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> getResponseById(@PathVariable Long id) {
        return ResponseEntity.ok(responseService.getResponseById(id));
    }

    // Update Response
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> updateResponse(
            @PathVariable Long id,
            @RequestBody ResponseDTO dto) {
        return ResponseEntity.ok(responseService.updateResponse(id, dto));
    }

    // Delete Response
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteResponse(@PathVariable Long id) {
        return ResponseEntity.ok(responseService.deleteResponse(id));
    }
}
