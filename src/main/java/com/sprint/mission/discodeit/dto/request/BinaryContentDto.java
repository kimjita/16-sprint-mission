package com.sprint.mission.discodeit.dto.request;

public record BinaryContentDto(
        String filename,
        Long size,
        String contentType,
        byte[] bytes
) {}
