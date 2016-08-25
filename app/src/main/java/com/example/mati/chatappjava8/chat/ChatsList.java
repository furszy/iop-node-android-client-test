package com.example.mati.chatappjava8.chat;

import java.util.UUID;

/**
 * Created by josej on 8/23/2016.
 */
public class ChatsList {
    private UUID chatId ;
    private String chatName;
    private String contactName ;
    private String contactId  ;
    private String lastmessage;
    private String status ;
    private String typeMessage  ;
    private Integer noReadMsgs   ;
    private String dateMessage;
    private byte[] imgProfile ;

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getContactName() {
        return contactName;
    }

    public  void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactId() {
        return contactId;
    }

    public  void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getLastMessage() {
        return lastmessage;
    }

    public void setLastMessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public Integer getNoReadMsgs() {
        return noReadMsgs;
    }

    public void setNoReadMsgs(Integer noReadMsgs) {
        this.noReadMsgs = noReadMsgs;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }

    public byte[] getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(byte[] imgProfile) {
        this.imgProfile = imgProfile;
    }


}
