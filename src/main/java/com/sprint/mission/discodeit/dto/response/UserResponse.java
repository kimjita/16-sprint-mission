package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String username,
        String email,
        boolean isOnline
) {}
