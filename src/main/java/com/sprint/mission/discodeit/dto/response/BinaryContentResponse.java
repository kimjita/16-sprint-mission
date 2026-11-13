package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String createdAt,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
) {

    public static BinaryContentResponse from(BinaryContent content) {
        return new BinaryContentResponse(content.getId(), content.getCreatedAt().toString(),
                content.getFileName(), content.getSize(), content.getContentType(), content.getBytes());
    }
}
