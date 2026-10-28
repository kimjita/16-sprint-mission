package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateDto;
import com.sprint.mission.discodeit.dto.response.ChannelCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.ChannelFindResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelCreateResponseDto createPublicChannel(PublicChannelCreateDto dto) {
        Channel channel = new Channel(ChannelType.PUBLIC, dto.name(), dto.description());
        channelRepository.save(channel);
        return new ChannelCreateResponseDto(channel.getId(), channel.getType(), channel.getName(), channel.getDescription());
    }

    @Override
    public ChannelCreateResponseDto createPrivateChannel(PrivateChannelCreateDto dto) {
        if (dto.userList() == null) {
            throw new IllegalArgumentException("참여자 없이 private 채널 생성은 불가능");
        }
        Channel channel = new Channel(ChannelType.PRIVATE, "", "");
        channelRepository.save(channel);
        List<UUID> userUUIDs = dto.userList();
        for (UUID userUUID : userUUIDs) {
            readStatusRepository.save(new ReadStatus(userUUID, channel.getId()));
        }
        return new ChannelCreateResponseDto(channel.getId(), channel.getType(), channel.getName(), channel.getDescription());
    }

    @Override
    public ChannelFindResponseDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 채널은 존재하지 않음"));
        Instant lastMessageAt = getLastMessageAt(channelId);

        List<UUID> participantIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            participantIds = getParticipantIds(channelId);
        }

        return new ChannelFindResponseDto(channel.getId(), channel.getType(), channel.getName(),
                channel.getDescription(), lastMessageAt, participantIds);
    }

    @Override
    public List<ChannelFindResponseDto> findAllByUserId(UUID userId) {
        List<ChannelFindResponseDto> result = new ArrayList<>();
        List<ReadStatus> statuses = readStatusRepository.findAll();
        List<Channel> channels = channelRepository.findAll();

        // PUBLIC channel부터 추가
        for (Channel channel : channels) {
            if (channel.getType() == ChannelType.PUBLIC) {
                result.add(new ChannelFindResponseDto(channel.getId(), channel.getType(), channel.getName(),
                        channel.getDescription(), getLastMessageAt(channel.getId()), getParticipantIds(channel.getId())));
            }
        }

        // PRIVATE channel 추가
        return result;
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }

    private List<UUID> getParticipantIds(UUID channelId) {
        List<UUID> participantIds = new ArrayList<>();
        List<ReadStatus> statuses = readStatusRepository.findAll();
        for (ReadStatus status : statuses) {
            if (channelId.equals(status.getChannelId())) {
                participantIds.add(status.getUserId());
            }
        }
        return participantIds;
    }

    // 채널의 가장 최근 메시지 시간, 메시지가 없으면 null
    private Instant getLastMessageAt(UUID channelId) {
        List<Message> messages = messageRepository.findAll();
        List<Message> channelMessages = new ArrayList<>();
        for (Message message : messages) {
            if (channelId.equals(message.getChannelId())) {
                channelMessages.add(message);
            }
        }

        if (channelMessages.isEmpty()) {
            return null;
        }

        Message lastMessage = channelMessages.get(0);
        for (Message channelMessage : channelMessages) {
            if (channelMessage.getCreatedAt().isAfter(lastMessage.getCreatedAt())) {
                lastMessage = channelMessage;
            }
        }

        return lastMessage.getCreatedAt();
    }
}
