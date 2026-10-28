package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto create(UserCreateDto dto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    UserResponseDto update(UUID id, UserUpdateDto dto);
    void delete(UUID userId);
}
