package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.custom.BadRequestException;
import com.sprint.mission.discodeit.exception.custom.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponse create(BinaryContentCreateRequest request) {
        validateOwnerExists(request.userId(), request.messageId());

        BinaryContent binaryContent = new BinaryContent(
            request.fileName(),
            request.contentType(),
            request.data(),
            request.userId(),
            request.messageId()
        );

        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
        return BinaryContentResponse.from(savedBinaryContent);
    }

    @Override
    public BinaryContentResponse find(UUID id) {
        return BinaryContentResponse.from(getBinaryContentOrThrow(id));
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<BinaryContent> binaryContents = binaryContentRepository.findByIdIn(ids);

        return binaryContents.stream()
            .map(BinaryContentResponse::from)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        getBinaryContentOrThrow(id);
        binaryContentRepository.delete(id);
    }

    private BinaryContent getBinaryContentOrThrow(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(ErrorCode.BINARYCONTENT_NOT_FOUND.format(id))
        );
    }

    private void validateOwnerExists(UUID userId, UUID messageId) {
        if (userId == null && messageId == null) {
            throw new BadRequestException(
                ErrorCode.USER_ID_AND_MESSAGE_ID_MUST_NOT_BE_NULL.getMessage());
        }

        if (userId != null) {
            userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.format(userId))
            );
        }

        if (messageId != null) {
            messageRepository.findById(messageId).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.MESSAGE_NOT_FOUND.format(messageId))
            );
        }

    }
}
