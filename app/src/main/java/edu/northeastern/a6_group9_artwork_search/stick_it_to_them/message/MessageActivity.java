package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message;

import android.os.Bundle;
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
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        String receiverUsername = getIntent().getStringExtra("RECEIVER_USERNAME");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        TextView chatPersonName = findViewById(R.id.chatPersonName);
        chatPersonName.setText(receiverUsername);

        FloatingActionButton fabShowStickers = findViewById(R.id.fab_show_stickers);
        fabShowStickers.setOnClickListener(view -> showStickerPicker());

        databaseClient = new RealtimeDatabaseClient(listener);

        currentUsername = getIntent().getStringExtra("CURRENT_USER_USERNAME");

        RecyclerView recyclerView = findViewById(R.id.messageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList, currentUsername);
        recyclerView.setAdapter(messageAdapter);

    }

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

    private DBClientListener listener = new DBClientListener() {
        @Override
        public void onUserLoggedIn(User user, String message) {
        }

        @Override
        public void onUserAdded(User user) {
        }

        @Override
        public void onMessageReceived(Message message) {
            messageList.add(message);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
        }

        @Override
        public void onCountStickersSentFinished(Map<String, Integer> result, String message) {
        }

        @Override
        public void onRetrieveReceivedMessagesFinished(List<Message> result, String message) {
        }
    };

    @Override
    public void onStickerSelected(String stickerResId) {
        String receiverUsername = getIntent().getStringExtra("RECEIVER_USERNAME");

        Message message = new Message(currentUsername, receiverUsername, stickerResId);

        databaseClient.sendMessage(message);

        messageList.add(message);
        messageAdapter.notifyItemInserted(messageList.size() - 1);

        // Close the StickerPickFragment
        StickerPickFragment existingFragment = (StickerPickFragment) getSupportFragmentManager().findFragmentById(R.id.stickerFragmentContainer);
        if (existingFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(existingFragment)
                    .commit();
        }
    }
}
