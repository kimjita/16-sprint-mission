package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto login(LoginDto dto) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            if (dto.username().equals(user.getUsername()) && dto.password().equals(user.getPassword())) {
                return createUserResponseDto(user);
            }
        }
        throw new IllegalArgumentException("해당 username:" + dto.username() + "와 password:" + dto.password() +
                "를 가진 유저가 존재하지 않음");
    }

    private UserStatus getUserStatus(User user) {
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 id(" + user.getId() + ")의 유저 상태를 찾을 수 없음"));
        return userStatus;
    }

    private UserResponseDto createUserResponseDto(User user) {
        UserStatus userStatus = getUserStatus(user);
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), userStatus.isOnline());
    }
}
