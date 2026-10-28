package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponseDto create(UserCreateDto newUser) {

        boolean isDuplicatedUsername = isDuplicatedUsername(newUser.username());
        boolean isDuplicatedEmail = isDuplicatedEmail(newUser.email());

        if (isDuplicatedUsername || isDuplicatedEmail) {
            throw new IllegalArgumentException("중복된 유저명 또는 이메일");
        }

        User user = new User(newUser.username(), newUser.email(), newUser.password());
        if (newUser.profile() != null) {
            BinaryContent binaryContent = createBinaryContent(newUser, user);
            binaryContentRepository.save(binaryContent);
        }

        User savedUser = userRepository.save(user);
        userStatusRepository.save(new UserStatus(savedUser.getId()));
        return createUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 id(" + userId + ")를 가진 유저를 찾을 수 없음"));
        return createUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> result = new ArrayList<>();
        for (User user : userList) {
            result.add(createUserResponseDto(user));
        }

        return result;
    }

    @Override
    public UserResponseDto update(UUID id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id(" + id + ")를 가진 유저를 찾을 수 없음"));

        boolean isDuplicatedUsername = isDuplicatedUsername(user.getUsername());
        boolean isDuplicatedEmail = isDuplicatedEmail(user.getEmail());

        if (isDuplicatedUsername || isDuplicatedEmail) {
            throw new IllegalArgumentException("중복된 유저명 또는 이메일");
        }

        UUID oldProfileId = user.getProfileId();
        UUID newProfileId = null;
        if (dto.profile() != null) {
            BinaryContent binaryContent = createBinaryContent(dto, user);
            binaryContentRepository.save(binaryContent);
            newProfileId = binaryContent.getId();

            if (oldProfileId != null) {
                binaryContentRepository.deleteById(oldProfileId);
            }
        }
        user.update(dto.newUsername(), dto.newEmail(), dto.newPassword(), newProfileId);
        User updatedUser = userRepository.save(user);
        return createUserResponseDto(updatedUser);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 id(" + userId + ")를 가진 유저를 찾을 수 없음"));

        // 프로필 이미지가 있으면 삭제
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.findByUserId(userId)
                .ifPresent(userStatus -> userStatusRepository.deleteById(userStatus.getId()));
        userRepository.deleteById(userId);
    }

    private boolean isDuplicatedUsername(String username) {
        List<User> userList = userRepository.findAll();
        return userList.stream().anyMatch(user -> username.equals(user.getUsername()));
    }

    private boolean isDuplicatedEmail(String email) {
        List<User> userList = userRepository.findAll();
        return userList.stream().anyMatch(user -> email.equals(user.getEmail()));
    }

    private BinaryContent createBinaryContent(UserCreateDto newUser, User user) {
        String filename = newUser.profile().filename();
        Long size = newUser.profile().size();
        String contentType = newUser.profile().contentType();
        byte[] bytes = newUser.profile().bytes();

        BinaryContent binaryContent = new BinaryContent(filename, size, contentType, bytes);
        user.setProfileId(binaryContent.getId());
        return binaryContent;
    }

    private BinaryContent createBinaryContent(UserUpdateDto updatedUser, User user) {
        String filename = updatedUser.profile().filename();
        Long size = updatedUser.profile().size();
        String contentType = updatedUser.profile().contentType();
        byte[] bytes = updatedUser.profile().bytes();

        BinaryContent binaryContent = new BinaryContent(filename, size, contentType, bytes);
        user.setProfileId(binaryContent.getId());
        return binaryContent;
    }

    private UserStatus getUserStatus(User user) {
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 id(" + user.getId() + ")의 유저 상태를 찾을 수 없음"));
        return userStatus;
    }

    // User를 온라인 상태가 포함된 응답 DTO로 변환
    private UserResponseDto createUserResponseDto(User user) {
        UserStatus userStatus = getUserStatus(user);
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), userStatus.isOnline());
    }
}
