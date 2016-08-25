package com.example.mati.chatappjava8.chat;

import java.util.UUID;

public class ChatMessage {
    private UUID id;
    private boolean isMe;
    private String message;
    private Long userId;
    private String dateTime;
    private boolean isFail;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public boolean getIsme() {
        return isMe;
    }
    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getIsFail() {
        return isFail;
    }

    public void setIsFail(boolean isFail) {
        this.isFail = isFail;
    }
}