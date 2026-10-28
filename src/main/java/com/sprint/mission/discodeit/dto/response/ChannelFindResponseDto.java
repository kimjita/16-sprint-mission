package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponseDto(
        UUID id,
        ChannelType channelType,
        String name,
        String description,
        Instant lastMessageAt,
        List<UUID> participantIds
) { }
