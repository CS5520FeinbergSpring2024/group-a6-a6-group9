package edu.northeastern.a6_group9_artwork_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}