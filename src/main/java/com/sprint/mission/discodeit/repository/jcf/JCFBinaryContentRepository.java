package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    prefix = "discodeit.repository",
    name = "type",
    havingValue = "jcf",
    matchIfMissing = true
)
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public List<BinaryContent> saveAll(List<BinaryContent> binaryContents) {
        for (BinaryContent binaryContent : binaryContents) {
            data.put(binaryContent.getId(), binaryContent);
        }

        return binaryContents;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findByIdIn(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();

        for (UUID id : ids) {
            BinaryContent binaryContent = data.get(id);
            if (binaryContent != null) {
                result.add(binaryContent);
            }
        }

        return result;
    }

    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        return data.values().stream()
            .filter(binaryContent -> Objects.equals(binaryContent.getMessageId(), messageId))
            .toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteAllByMessageId(UUID messageId) {
        data.entrySet().removeIf(
            entry -> Objects.equals(entry.getValue().getMessageId(), messageId)
        );
    }
}
