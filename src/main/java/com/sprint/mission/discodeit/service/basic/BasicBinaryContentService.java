package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
        return dtoFrom(savedBinaryContent);
    }

    @Override
    public BinaryContentResponse find(UUID id) {
        BinaryContent binaryContent = getBinaryContentOrThrow(id);

        return dtoFrom(binaryContent);
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<BinaryContent> binaryContents = binaryContentRepository.findByIdIn(ids);

        return binaryContents.stream()
            .map(this::dtoFrom)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        getBinaryContentOrThrow(id);
        binaryContentRepository.delete(id);
    }

    private BinaryContent getBinaryContentOrThrow(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("파일 없음")
        );
    }

    private void validateOwnerExists(UUID userId, UUID messageId) {
        if (userId == null && messageId == null) {
            throw new IllegalArgumentException("userId와 messageId는 둘 다 null일 수 없습니다.");
        }

        if (userId != null) {
            userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저 존재 안함")
            );
        }

        if (messageId != null) {
            messageRepository.findById(messageId).orElseThrow(
                () -> new IllegalArgumentException("메세지 존재 안함")
            );
        }

    }

    private BinaryContentResponse dtoFrom(BinaryContent binaryContent){
        return new BinaryContentResponse(
            binaryContent.getId(),
            binaryContent.getFileName(),
            binaryContent.getContentType(),
            binaryContent.getData(),
            binaryContent.getUserId(),
            binaryContent.getMessageId()
        );
    }
}
