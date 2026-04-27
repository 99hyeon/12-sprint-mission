package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    @RequestMapping(value = "/api/messages", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createMessage(
        @RequestPart("messageCreateRequest") MessageCreateRequest request,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(messageService.create(request, attachments));
    }

    @Override
    @RequestMapping(value = "/api/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable("messageId") UUID messageId,
        @RequestBody MessageUpdateRequest request) {
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

    @Override
    @RequestMapping(value = "/api/messages/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequestMapping(value = "/api/messages", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> findMessagesByChannelId(
        @RequestParam("channelId") UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

}
