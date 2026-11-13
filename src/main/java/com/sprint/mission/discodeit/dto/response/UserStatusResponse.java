package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        String createdAt,
        String updatedAt,
        UUID userId,
        String lastActiveAt,
        boolean online
) {

    public static UserStatusResponse from(UserStatus status) {
        return new UserStatusResponse(status.getId(), status.getCreatedAt().toString(),
                status.getUpdatedAt().toString(), status.getUserId(), status.getLastActiveAt().toString(),
                status.isOnline());
    }
}
