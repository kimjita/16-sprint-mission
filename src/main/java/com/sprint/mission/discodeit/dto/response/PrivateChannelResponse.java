package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record PrivateChannelResponse(
        UUID id,
        String createdAt,
        String updatedAt,
        ChannelType type
) {
    public static PrivateChannelResponse from(Channel channel) {
        return new PrivateChannelResponse(channel.getId(), channel.getCreatedAt().toString(),
                channel.getUpdatedAt().toString(), channel.getType());
    }
}
