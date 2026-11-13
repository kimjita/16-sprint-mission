package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserResponse> postUser(@RequestPart("request") CreateUserRequest request,
                                                 @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

        BinaryContentRequest contentRequest = null;
        if (profile != null) {
            contentRequest = new BinaryContentRequest(profile.getOriginalFilename(), profile.getSize(),
                    profile.getContentType(), profile.getBytes());
        }

        UserResponse response = userService.create(contentRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> response = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(@RequestPart("request") UpdateUserRequest request,
                                                           @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                           @PathVariable UUID userId) throws IOException {
        BinaryContentRequest contentRequest = null;
        if (profile != null) {
            contentRequest = new BinaryContentRequest(profile.getOriginalFilename(), profile.getSize(),
                    profile.getContentType(), profile.getBytes());
        }
        UserResponse response = userService.update(userId, contentRequest, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponse> updateUserOnlineStatus(@RequestBody UpdateUserStatusRequest request,
                                                               @PathVariable UUID userId) {
        UserStatusResponse response = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
