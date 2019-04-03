package top.starrysea.object.dto;

import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;

public class User implements Entity {
    private String userId;
    private String userEmail;
    private String userPassword;
    private String username;
    private short osu1;
    private short osu2;
    private short osu3;

    private User(Builder builder) {
        this.userId = builder.userId;
        this.userEmail = builder.userEmail;
        this.userPassword = builder.userPassword;
        this.username = builder.username;
        this.osu1 = builder.osu1;
        this.osu2 = builder.osu2;
        this.osu3 = builder.osu3;
    }

    public static class Builder implements IBuilder<User> {
        private String userId;
        private String userEmail;
        private String userPassword;
        private String username;
        private short osu1;
        private short osu2;
        private short osu3;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder userPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder osu1(short osu1) {
            this.osu1 = osu1;
            return this;
        }

        public Builder osu2(short osu2) {
            this.osu2 = osu2;
            return this;
        }

        public Builder osu3(short osu3) {
            this.osu3 = osu3;
            return this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public short getOsu1() {
        return osu1;
    }

    public void setOsu1(short osu1) {
        this.osu1 = osu1;
    }

    public short getOsu2() {
        return osu2;
    }

    public void setOsu2(short osu2) {
        this.osu2 = osu2;
    }

    public short getOsu3() {
        return osu3;
    }

    public void setOsu3(short osu3) {
        this.osu3 = osu3;
    }
}
