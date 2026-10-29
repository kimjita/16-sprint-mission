package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequest;

public interface ReadStatusService {
    void create(CreateReadStatusRequest request);

    void find();

    void findAllByUserId();

    void update();

    void delete();
}
