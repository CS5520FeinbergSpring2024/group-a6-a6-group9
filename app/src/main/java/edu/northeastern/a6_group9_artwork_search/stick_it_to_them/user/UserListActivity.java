package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.a6_group9_artwork_search.R;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.Message;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.MessageActivity;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.ReceivedMessageActivity;

public class UserListActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {
    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private String currentUsername;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ImageButton logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        ImageButton historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(view -> showDetailsPopup());

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList, this);
        usersRecyclerView.setAdapter(userAdapter);
        currentUsername = getIntent().getStringExtra("CURRENT_USER_USERNAME");

        fetchUsers();

        newMessageNotification();
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("RECEIVER_USERNAME", user.getUsername());
        intent.putExtra("CURRENT_USER_USERNAME", currentUsername);
        startActivity(intent);
    }

    private void fetchUsers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && !user.getUsername().equals(currentUsername)) {
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserListActivity", "Database error", databaseError.toException());
            }
        });
    }

    private void newMessageNotification() {
        DatabaseReference msgDatabaseRef = FirebaseDatabase.getInstance().getReference("messages");

        msgDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null && message.getReceiverUsername().equals(currentUsername)) {
                        Snackbar.make(usersRecyclerView, "You have a new message",
                                        Snackbar.LENGTH_LONG)
                                .setAction("View", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewMessageHistory(v);
                                    }
                                }).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void showDetailsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details");

        // Define the buttons and their onClick logic
        builder.setItems(new CharSequence[]{"Message History", "Show Stickers Count"}, (dialog, which) -> {
            switch (which) {
                case 0: // Message History clicked
                    viewMessageHistory(); // Implement this method to show message history
                    break;
                case 1: // Show Stickers Count clicked
                    fetchAndShowStickersCount(); // Implement this method to show stickers count
                    break;
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Placeholder methods for the actions
    private void viewMessageHistory() {
        Intent intent = new Intent(this, ReceivedMessageActivity.class);
        intent.putExtra("CURRENT_USER_USERNAME", currentUsername);
        startActivity(intent);
    }

    private void fetchAndShowStickersCount() {

    }

    @Override
    public void onCountStickersSentFinished(Map<String, Integer> result, String message) {
        runOnUiThread(() -> {
            if (result != null && !result.isEmpty()) {
                StringBuilder stickersCountBuilder = new StringBuilder("Stickers sent:\n");
                for (Map.Entry<String, Integer> entry : result.entrySet()) {
                    stickersCountBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                // Show an AlertDialog with the count
                AlertDialog show = new AlertDialog.Builder(MessageActivity.this)
                        .setTitle("Sticker Counts")
                        .setMessage(stickersCountBuilder.toString())
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                // Handle the case where no stickers were sent or there was an error
                AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this)
                        .setTitle("Sticker Counts")
                        .setMessage("Error or no stickers sent")
                        .setPositiveButton("OK", null)
                        .show();
                Log.e("MessageActivity", "Error or no stickers sent: " + message);
            }
        });
    }
}