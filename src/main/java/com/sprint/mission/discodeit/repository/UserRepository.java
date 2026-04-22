package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(java.util.UUID id);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void delete(java.util.UUID id);
}
