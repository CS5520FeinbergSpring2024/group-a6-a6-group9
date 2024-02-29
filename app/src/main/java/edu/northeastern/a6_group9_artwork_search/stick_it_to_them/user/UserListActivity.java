package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.a6_group9_artwork_search.R;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
