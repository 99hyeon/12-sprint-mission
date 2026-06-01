package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @Override
    @PostMapping(value = "/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(
        @RequestBody ChannelPublicCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublic(request));
    }

    @Override
    @PostMapping(value = "/private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(
        @RequestBody ChannelPrivateCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelService.createPrivate(request));
    }

    @Override
    @PatchMapping(value = "/{channelId}")
    public ResponseEntity<ChannelResponse> updateChannel(
        @PathVariable("channelId") UUID channelId,
        @RequestBody ChannelUpdateRequest request) {
        return ResponseEntity.ok(channelService.update(channelId, request));
    }

    @Override
    @DeleteMapping(value = "/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ChannelResponse>> findAllByUserId(
        @RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

}
