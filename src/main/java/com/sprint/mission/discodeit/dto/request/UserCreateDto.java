package com.sprint.mission.discodeit.dto.request;

public record UserCreateDto(
        String username,
        String email,
        String password,
        BinaryContentDto profile
) {}
