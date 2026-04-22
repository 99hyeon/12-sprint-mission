package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseEntity {

    private String email;
    private String nickName;
    private String userName;
    private String password;
    private UUID profileImageId;
    private Instant updatedAt;

    public User(String email, String nickName, String userName, String password, UUID profileImageId) {
        super();
        this.email = email;
        this.nickName = nickName;
        this.userName = userName;
        this.password = password;
        this.profileImageId = profileImageId;
        this.updatedAt = Instant.now();
    }

    public void updateEmail(String email) {
        this.email = email;
        updateUpdatedAt();
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
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

    public void updateProfile(String email, String nickName, String userName, UUID profileImageId){
        this.email = email;
        this.nickName = nickName;
        this.userName = userName;
        this.profileImageId = profileImageId;
        updateUpdatedAt();
    }

    private void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
