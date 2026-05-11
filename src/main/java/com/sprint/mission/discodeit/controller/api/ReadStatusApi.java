package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "ReadStatus", description = "ReadStatus API")
public interface ReadStatusApi {

    @Operation(summary = "Message 읽음 상태 생성")
    @ApiResponse(responseCode = "201", description = "Message 읽음 상태 생성 성공")
    ResponseEntity<ReadStatusResponse> createReadStatus(
        ReadStatusCreateRequest request
    );

    @Operation(summary = "Message 읽음 상태 수정")
    @ApiResponse(responseCode = "200", description = "Message 읽음 상태 수정 성공")
    ResponseEntity<ReadStatusResponse> updateReadStatus(
        UUID readStatusId,
        ReadStatusUpdateRequest request
    );

    @Operation(summary = "User의 Message 읽음 상태 목록 조회")
    @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
    ResponseEntity<List<ReadStatusResponse>> findReadStatusByUserId(
        UUID userId
    );

}
