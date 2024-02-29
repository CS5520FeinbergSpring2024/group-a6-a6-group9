package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.northeastern.a6_group9_artwork_search.R;

public class ChatActivity extends AppCompatActivity implements StickerPickFragment.OnStickerSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String username = getIntent().getStringExtra("USERNAME");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        TextView chatPersonName = findViewById(R.id.chatPersonName);
        chatPersonName.setText(username);

        FloatingActionButton fabShowStickers = findViewById(R.id.fab_show_stickers);
        fabShowStickers.setOnClickListener(view -> showStickerPicker());
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

    @Override
    public void onStickerSelected(int stickerResId) {
        // Handle sticker selection (e.g., send sticker message, display in RecyclerView)
        // You'll need to implement sending and displaying stickers in your chat messages here

        StickerPickFragment existingFragment = (StickerPickFragment) getSupportFragmentManager().findFragmentById(R.id.stickerFragmentContainer);
        if (existingFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(existingFragment)
                    .commit();
        }
    }
}
