package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UpdateUserRequest(
        UUID id,
        String newUsername,
        String newEmail,
        String newPassword,
        BinaryContentRequest profile
) {}
