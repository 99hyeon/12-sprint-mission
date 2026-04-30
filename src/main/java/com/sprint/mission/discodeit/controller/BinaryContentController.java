package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/api/binaryContent/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getFile(@RequestParam("binaryContentId") UUID binaryContentId){
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

    @RequestMapping(value = "/api/binaryContents", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponse>> getFiles(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

}
