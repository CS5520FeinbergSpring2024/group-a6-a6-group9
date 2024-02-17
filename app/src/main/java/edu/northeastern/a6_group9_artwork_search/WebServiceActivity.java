package edu.northeastern.a6_group9_artwork_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Calendar;

public class WebServiceActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText artistEditText;
    private NumberPicker yearPicker;
    private String[] userInputs = new String[3];  //usersInput[0] is title, [1] is artist, [2] is year

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        titleEditText = (EditText) findViewById(R.id.et_title);
        artistEditText = (EditText) findViewById(R.id.et_artist);

        // Initialize the DatePicker
        yearPicker = (NumberPicker) findViewById(R.id.yearPicker);
        yearPicker.setMaxValue(2024);
        yearPicker.setMinValue(1700);
        yearPicker.setValue(2024);

        Button searchBtn = findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(v -> callWebserviceButtonHandler());
    }

    public void callWebserviceButtonHandler(){
        userInputs[0] = titleEditText.getText().toString().trim();
        userInputs[1] = artistEditText.getText().toString().trim();
        userInputs[2] = String.valueOf(yearPicker.getValue());

        Intent intent = new Intent(WebServiceActivity.this, ResultsActivity.class);
        intent.putExtra("title", userInputs[0]);
        intent.putExtra("artist", userInputs[1]);
        intent.putExtra("year", userInputs[2]);
        startActivity(intent);
    }
}