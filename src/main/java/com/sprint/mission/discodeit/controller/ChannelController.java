package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ApiResponse;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.response.PrivateChannelResponse;
import com.sprint.mission.discodeit.dto.response.FindChannelResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(@RequestBody CreatePublicChannelRequest request) {
        ChannelResponse response = channelService.createPublicChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/private")
    public ResponseEntity<PrivateChannelResponse> createPrivateChannel(@RequestBody CreatePrivateChannelRequest request) {
        PrivateChannelResponse response = channelService.createPrivateChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updateChannel(@RequestBody UpdateChannelRequest request,
                                                         @PathVariable UUID channelId) {
        ChannelResponse response = channelService.update(channelId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<FindChannelResponse>> getChannels(@RequestParam UUID userId) {
        List<FindChannelResponse> response = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
