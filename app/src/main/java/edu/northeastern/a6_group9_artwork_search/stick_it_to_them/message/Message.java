package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Message {
    private String senderUsername;
    private String receiverUsername;
    private String stickerId;
    private Date sendTime;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Message() {}

    public Message(String senderUsername, String receiverUsername, String stickerId) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.stickerId = stickerId;
        this.sendTime = new Date();
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getStickerId() {
        return stickerId;
    }

    public Date getSendTime() {
        return sendTime;
    }
}
