package edu.northeastern.a6_group9_artwork_search.stick_it_to_them;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.Message;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.User;

public class RealtimeDatabaseClient {
    private final String logTag = "RealtimeDatabaseClient";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference userDatabaseReference = database.getReference("users");
    private final DatabaseReference messageDatabaseReference = database.getReference("messages");
    private final DBClientListener listener;
    private String currentUsername;

    public RealtimeDatabaseClient(@NonNull DBClientListener listener) {
        this.listener = listener;
        userDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    listener.onUserAdded(user);
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
        messageDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    if (message.getReceiverUsername().equals(currentUsername)) {
                        listener.onMessageReceived(message);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                Log.d(logTag, "message onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    public void loginUser(String username) {
        userDatabaseReference.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                if (user == null) {
                    user = new User(username);
                    userDatabaseReference.child(username).setValue(user);
                }
                currentUsername = username;
                listener.onUserLoggedIn(user, null);
            } else {
                String message = "Error getting data" + task.getException();
                Log.e(logTag, message);
                listener.onUserLoggedIn(null, message);
            }
        });
    }

    public void sendMessage(Message message) {
        messageDatabaseReference.push().setValue(message);
    }

    public void countStickersSent(User user) {
        messageDatabaseReference.orderByChild("senderUsername").equalTo(user.getUsername()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> result = new HashMap<>();
                for (DataSnapshot record : task.getResult().getChildren()) {
                    Message message = record.getValue(Message.class);
                    assert message != null;
                    Integer currentCount = result.getOrDefault(message.getStickerId(), 0);
                    assert currentCount != null;
                    result.put(message.getStickerId(), currentCount + 1);
                }
                listener.onCountStickersSentFinished(result, null);
            } else {
                String message = "Error getting data" + task.getException();
                Log.e(logTag, message);
                listener.onCountStickersSentFinished(null, message);
            }
        });
    }

    // retrieve all messages related to sender
    public void retrieveConversationMessages(String currentUserUsername, String otherUserUsername) {
        List<Message> conversationMessages = new ArrayList<>();

        messageDatabaseReference.orderByChild("senderUsername").equalTo(currentUserUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            if (message != null && otherUserUsername.equals(message.getReceiverUsername())) {
                                conversationMessages.add(message);
                            }
                        }
                        fetchReceivedMessages(currentUserUsername, otherUserUsername, conversationMessages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onRetrieveReceivedMessagesFinished(null, databaseError.getMessage());
                    }
                });
    }

    // from sender messages, filter messages to receiver
    private void fetchReceivedMessages(String currentUserUsername, String otherUserUsername, List<Message> conversationMessages) {
        messageDatabaseReference.orderByChild("receiverUsername").equalTo(currentUserUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            if (message != null && otherUserUsername.equals(message.getSenderUsername())) {
                                conversationMessages.add(message);
                            }
                        }
                        listener.onRetrieveReceivedMessagesFinished(conversationMessages, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onRetrieveReceivedMessagesFinished(null, databaseError.getMessage());
                    }
                });
    }

}
