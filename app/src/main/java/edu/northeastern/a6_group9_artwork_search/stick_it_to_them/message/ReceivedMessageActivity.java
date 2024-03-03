package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.User;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.UserAdapter;

public class ReceivedMessageActivity extends AppCompatActivity {
    private RecyclerView receivedMessageRecyclerView;
    private ReceivedMessageAdapter receivedMessageAdapter;
    private String currentUsername;
    private List<Message> receivedMessageList;
    DatabaseReference messageDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_message);

        messageDatabaseRef = FirebaseDatabase.getInstance().getReference("messages");
        currentUsername = getIntent().getStringExtra("CURRENT_USER_USERNAME");
        receivedMessageList = new ArrayList<>();

        receivedMessageRecyclerView = findViewById(R.id.received_message_list);
        receivedMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        receivedMessageAdapter = new ReceivedMessageAdapter(this, receivedMessageList);
        receivedMessageRecyclerView.setAdapter(receivedMessageAdapter);

        fetchReceivedMessage();
    }

    private void fetchReceivedMessage() {
        messageDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receivedMessageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null && message.getReceiverUsername().equals(currentUsername)) {
                        receivedMessageList.add(message);
                        receivedMessageAdapter.notifyItemInserted(receivedMessageList.size() - 1);
                        receivedMessageRecyclerView.scrollToPosition(receivedMessageList.size() - 1);
                    }
                }
                receivedMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ReceivedMessageActivity", "Database error", databaseError.toException());
            }
        });
    }
}