package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        String createdAt,
        String updatedAt,
        UUID userId,
        UUID channelId,
        String lastReadAt
) {

    public static ReadStatusResponse from(ReadStatus status) {
        return new ReadStatusResponse(status.getId(), status.getCreatedAt().toString(),
                status.getUpdatedAt().toString(), status.getUserId(),
                status.getChannelId(), status.getLastReadAt().toString());
    }
}
