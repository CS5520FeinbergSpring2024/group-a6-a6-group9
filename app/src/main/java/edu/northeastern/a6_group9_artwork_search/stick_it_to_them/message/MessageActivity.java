package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.a6_group9_artwork_search.R;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.DBClientListener;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.RealtimeDatabaseClient;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.sticker.StickerPickFragment;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.User;

public class MessageActivity extends AppCompatActivity implements StickerPickFragment.OnStickerSelectedListener {

    private RealtimeDatabaseClient databaseClient;
    private String currentUsername;
    private String receiverUsername;
    private RecyclerView messageRecyclerView;

    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        receiverUsername = getIntent().getStringExtra("RECEIVER_USERNAME");
        currentUsername = getIntent().getStringExtra("CURRENT_USER_USERNAME");

        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList, currentUsername);
        messageRecyclerView.setAdapter(messageAdapter);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        TextView chatPersonName = findViewById(R.id.chatPersonName);
        chatPersonName.setText(receiverUsername);

        FloatingActionButton fabShowStickers = findViewById(R.id.fab_show_stickers);
        fabShowStickers.setOnClickListener(view -> showStickerPicker());

        databaseClient = new RealtimeDatabaseClient(listener);
        databaseClient.fetchMessagesBetweenTwoUsers(currentUsername, receiverUsername);
    }

    private DBClientListener listener = new DBClientListener() {
        @Override
        public void onUserLoggedIn(User user, String message) {
        }

        @Override
        public void onUserAdded(User user) {
        }

        @Override
        public void onMessageReceived(Message message) {
            if ((message.getReceiverUsername().equals(currentUsername)) && message.getSenderUsername().equals(receiverUsername)) {
                Log.d("MessageActivity", "sender: " + message.getSenderUsername() + "   receiver: " + message.getReceiverUsername());
                messageList.add(message);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                messageRecyclerView.scrollToPosition(messageList.size() - 1);
            }
        }

        @Override
        public void onCountStickersSentFinished(Map<String, Integer> result, String message) {
        }

        @Override
        public void onRetrieveReceivedMessagesFinished(List<Message> result, String message) {
            if (result != null) {
                messageList.clear();
                messageList.addAll(result);
                messageAdapter.notifyDataSetChanged();
            } else {
                Log.e("MessageActivity", "Error retrieving messages: " + message);
            }
        }
    };

    private void showStickerPicker() {
        StickerPickFragment existingFragment = (StickerPickFragment) getSupportFragmentManager().findFragmentById(R.id.stickerFragmentContainer);

        if (existingFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(existingFragment)
                    .commit();
        } else {
            StickerPickFragment stickerPickFragment = new StickerPickFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.stickerFragmentContainer, stickerPickFragment)
                    .commit();
        }
    }

    @Override
    public void onStickerSelected(String stickerResId) {
        Message message = new Message(currentUsername, receiverUsername, stickerResId);

        databaseClient.sendMessage(message);

        messageList.add(message);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        messageRecyclerView.scrollToPosition(messageList.size() - 1);

        // Close the StickerPickFragment
        StickerPickFragment existingFragment = (StickerPickFragment) getSupportFragmentManager().findFragmentById(R.id.stickerFragmentContainer);
        if (existingFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(existingFragment)
                    .commit();
        }
    }
}
