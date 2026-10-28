package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String username,
        String email,
        boolean isOnline
) {}
