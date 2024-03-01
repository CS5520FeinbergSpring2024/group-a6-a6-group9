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
                messageDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        if (message != null) {
                            if (message.getReceiverUsername().equals(currentUsername) || message.getSenderUsername().equals(currentUsername)) {
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

    // used to retrieve all messages received by user (for history)
    public void retrieveReceivedMessages(User user) {
        messageDatabaseReference.orderByChild("receiverUsername").equalTo(user.getUsername()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Message> result = new ArrayList<>();
                for (DataSnapshot record : task.getResult().getChildren()) {
                    result.add(record.getValue(Message.class));
                }
                listener.onRetrieveReceivedMessagesFinished(result, null);
            } else {
                String message = "Error getting data" + task.getException();
                Log.e(logTag, message);
                listener.onRetrieveReceivedMessagesFinished(null, message);
            }
        });
    }

    // fetch messages related to two users only
    public void fetchMessagesBetweenTwoUsers(String currentName, String receiverName) {
        List<Message> messages = new ArrayList<>();
        messageDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null &&
                            (currentName.equals(message.getSenderUsername()) && receiverName.equals(message.getReceiverUsername()) ||
                                    currentName.equals(message.getReceiverUsername()) && receiverName.equals(message.getSenderUsername()))) {
                        messages.add(message);
                    }
                }
                listener.onRetrieveReceivedMessagesFinished(messages, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onRetrieveReceivedMessagesFinished(null, databaseError.getMessage());
            }
        });
    }
}
