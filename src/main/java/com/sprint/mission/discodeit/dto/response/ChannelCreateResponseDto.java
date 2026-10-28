package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelCreateResponseDto(
        UUID id,
        ChannelType channelType,
        String name,
        String description
) { }
