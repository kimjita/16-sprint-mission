package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        String createdAt,
        String updatedAt,
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getId(), message.getCreatedAt().toString(),
                message.getUpdatedAt().toString(), message.getContent(), message.getChannelId(),
                message.getAuthorId(), message.getAttachmentIds());
    }
}
