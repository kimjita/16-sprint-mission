package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusResponse> createReadStatus(@RequestBody CreateReadStatusRequest request) {
        ReadStatusResponse response = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponse> updateReadStatus(@RequestBody UpdateReadStatusRequest request,
                                                               @PathVariable UUID readStatusId) {
        ReadStatusResponse response = readStatusService.update(readStatusId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResponse>> getReadStatus(@RequestParam UUID userId) {
        List<ReadStatusResponse> response = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
