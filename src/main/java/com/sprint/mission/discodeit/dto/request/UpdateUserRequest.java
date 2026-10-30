package com.sprint.mission.discodeit.dto.request;

public record UpdateUserRequest(
        String newUsername,
        String newEmail,
        String newPassword,
        BinaryContentRequest profile
) {}
