package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "BinaryContent API")
public interface BinaryContentApi {

    @Operation(summary = "첨부 파일 조회")
    @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공")
    ResponseEntity<BinaryContentResponse> getFile(
        UUID binaryContentId
    );

    @Operation(summary = "여러 첨부 파일 조회")
    @ApiResponse(responseCode = "200", description = "여러 첨부 파일 조회 성공")
    ResponseEntity<List<BinaryContentResponse>> getFiles(
        List<UUID> binaryContentIds
    );

}
