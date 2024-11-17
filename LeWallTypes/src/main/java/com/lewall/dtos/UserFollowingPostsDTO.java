package com.lewall.dtos;

import java.util.UUID;

public class UserFollowingPostsDTO {
    private UUID userId;
    private UUID classId;

    public UserFollowingPostsDTO(UUID userId, UUID classId) {
        this.userId = userId;
        this.classId = classId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getClassId() {
        return classId;
    }
}
