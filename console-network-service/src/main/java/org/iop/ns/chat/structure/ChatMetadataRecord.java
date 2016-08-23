package org.iop.ns.chat.structure;

import java.sql.Timestamp;
import java.util.UUID;

public class ChatMetadataRecord {

    private UUID id;

    private String localActorPublicKey;

    private String remoteActorPublicKey;

    private Timestamp date;

    private String message;

    public ChatMetadataRecord(UUID id, String localActorPublicKey, String remoteActorPublicKey, Timestamp date, String message) {
        this.id = id;
        this.localActorPublicKey = localActorPublicKey;
        this.remoteActorPublicKey = remoteActorPublicKey;
        this.date = date;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getLocalActorPublicKey() {
        return localActorPublicKey;
    }

    public String getRemoteActorPublicKey() {
        return remoteActorPublicKey;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatMetadataRecord{" +
                "id=" + id +
                ", localActorPublicKey='" + localActorPublicKey + '\'' +
                ", remoteActorPublicKey='" + remoteActorPublicKey + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
