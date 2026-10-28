package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserUpdateDto(
        UUID id,
        String newUsername,
        String newEmail,
        String newPassword,
        BinaryContentDto profile
) {}
