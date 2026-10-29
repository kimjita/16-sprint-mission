package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.response.CreatePrivateChannelResponse;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.response.FindChannelResponse;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponse createPublicChannel(PublicChannelCreateRequest request);
    CreatePrivateChannelResponse createPrivateChannel(PrivateChannelCreateRequest request);
    FindChannelResponse find(UUID channelId);
    List<FindChannelResponse> findAllByUserId(UUID userId);
    ChannelResponse update(ChannelUpdateRequest request);
    void delete(UUID channelId);
}
