package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseEntity {

    private String email;
    private String userName;
    private String password;
    private UUID profileImageId;
    private Instant updatedAt;

    public User(String email, String userName, String password, UUID profileImageId) {
        super();
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.profileImageId = profileImageId;
        this.updatedAt = Instant.now();
    }

    public void updateEmail(String email) {
        this.email = email;
        updateUpdatedAt();
    }

    public void updateUserName(String userName) {
        this.userName = userName;
        updateUpdatedAt();
    }

    public void updateProfileImageId(UUID profileImageId) {
        this.profileImageId = profileImageId;
        updateUpdatedAt();
    }

    public void changeProfile(String email, String userName, UUID profileImageId){
        if (email != null) {
            this.email = email;
        }

        if (userName != null) {
            this.userName = userName;
        }

        if (profileImageId != null) {
            this.profileImageId = profileImageId;
        }

        updateUpdatedAt();
    }

    public void removeProfileImg() {
        this.profileImageId = null;
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
