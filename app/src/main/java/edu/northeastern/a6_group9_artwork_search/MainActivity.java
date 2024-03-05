package edu.northeastern.a6_group9_artwork_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.UserLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startWebService(View view) {
        Intent intent = new Intent(this, WebServiceActivity.class);
        startActivity(intent);
    }

    public void startSendStickers(View view) {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }

    public void startAboutMe(View view) {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }
}