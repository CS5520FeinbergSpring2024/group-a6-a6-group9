package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.a6_group9_artwork_search.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
