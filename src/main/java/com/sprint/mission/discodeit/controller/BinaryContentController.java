package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;

    @Override
    @RequestMapping(value = "/api/binaryContents/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponse> getFile(
        @PathVariable("binaryContentId") UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

    @Override
    @RequestMapping(value = "/api/binaryContents", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponse>> getFiles(
        @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

}
