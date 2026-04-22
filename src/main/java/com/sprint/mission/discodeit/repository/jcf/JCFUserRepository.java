package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    prefix = "discodeit.repository",
    name = "type",
    havingValue = "jcf",
    matchIfMissing = true
)
public class JCFUserRepository implements UserRepository {
    private final Map<java.util.UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(java.util.UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return data.values().stream()
            .filter(user -> user.getUserName().equals(userName))
            .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.values().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(java.util.UUID id) {
        data.remove(id);
    }
}
