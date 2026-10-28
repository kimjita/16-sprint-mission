package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateDto;
import com.sprint.mission.discodeit.dto.response.ChannelCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.ChannelFindResponseDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelCreateResponseDto createPublicChannel(PublicChannelCreateDto dto);
    ChannelCreateResponseDto createPrivateChannel(PrivateChannelCreateDto dto);
    ChannelFindResponseDto find(UUID channelId);
    List<ChannelFindResponseDto> findAllByUserId(UUID userId);
    Channel update(UUID channelId, String newName, String newDescription);
    void delete(UUID channelId);
}
