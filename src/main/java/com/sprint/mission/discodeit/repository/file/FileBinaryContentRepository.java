package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    prefix = "discodeit.repository",
    name = "type",
    havingValue = "file"
)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String TARGET_NAME = "BinaryContent";
    private static final String FILE_PATH = "/binaryContents.ser";

    private final FileStore<Map<UUID, BinaryContent>> fileStore;

    public FileBinaryContentRepository(
        @Value("${discodeit.repository.file-directory:data}") String fileDirectory
    ) {
        this.fileStore = new FileStore<>(fileDirectory + FILE_PATH, TARGET_NAME);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> data = loadOrEmpty();
        data.put(binaryContent.getId(), binaryContent);
        fileStore.save(data);

        return binaryContent;
    }

    @Override
    public List<BinaryContent> saveAll(List<BinaryContent> binaryContents) {
        Map<UUID, BinaryContent> data = loadOrEmpty();

        for (BinaryContent binaryContent : binaryContents) {
            data.put(binaryContent.getId(), binaryContent);
        }

        fileStore.save(data);
        return binaryContents;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(loadOrEmpty().get(id));
    }

    @Override
    public List<BinaryContent> findByIdIn(List<UUID> ids) {
        Map<UUID, BinaryContent> data = loadOrEmpty();
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
        return loadOrEmpty().values().stream()
            .filter(binaryContent -> Objects.equals(binaryContent.getMessageId(), messageId))
            .toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(loadOrEmpty().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, BinaryContent> data = loadOrEmpty();
        data.remove(id);
        fileStore.save(data);
    }

    @Override
    public void deleteAllByMessageId(UUID messageId) {
        Map<UUID, BinaryContent> data = loadOrEmpty();

        data.entrySet().removeIf(
            entry -> Objects.equals(entry.getValue().getMessageId(), messageId)
        );

        fileStore.save(data);
    }

    private Map<UUID, BinaryContent> loadOrEmpty() {
        Map<UUID, BinaryContent> data = fileStore.load();
        return data == null ? new HashMap<>() : data;
    }
}
