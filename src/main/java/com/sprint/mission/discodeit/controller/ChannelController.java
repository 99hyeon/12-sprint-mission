package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/api/channels/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createPublicChannel(
        @RequestBody ChannelPublicCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPublic(request));
    }

    @RequestMapping(value = "/api/channels/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createPrivateChannel(
        @RequestBody ChannelPrivateCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createPrivate(request));
    }

    @RequestMapping(value = "/api/channels/{channelId}", method = RequestMethod.PATCH)
    public ResponseEntity<ChannelResponse> updateChannel(@PathVariable("channelId") UUID channelId,
        @RequestBody ChannelUpdateRequest request){
        return ResponseEntity.ok(channelService.update(channelId, request));
    }

    @RequestMapping(value = "/api/channels/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable("channelId") UUID channelId){
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/api/channels", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelFindResponse>> findAllByUserId(@RequestParam("userId") UUID userId){
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

}
