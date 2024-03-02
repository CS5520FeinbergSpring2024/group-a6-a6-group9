package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.MessageActivity;

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

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList, this);
        usersRecyclerView.setAdapter(userAdapter);
        currentUsername = getIntent().getStringExtra("CURRENT_USER_USERNAME");

        fetchUsers();
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
}
