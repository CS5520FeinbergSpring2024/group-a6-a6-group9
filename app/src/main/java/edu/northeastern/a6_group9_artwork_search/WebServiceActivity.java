package edu.northeastern.a6_group9_artwork_search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class WebServiceActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText artistEditText;
    private DatePicker yearPicker;
    private String[] userInputs = new String[3];  //usersInput[0] is title, [1] is artist, [2] is year

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        titleEditText = (EditText) findViewById(R.id.et_title);
        artistEditText = (EditText) findViewById(R.id.et_artist);

        // Initialize the DatePicker
        yearPicker = (DatePicker) findViewById(R.id.yearPicker);
        // Set the initial value
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        yearPicker.init(currentYear, currentMonth, currentDay, null);

        // Set OnDateChangedListener to handle the selected year
        yearPicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) ->
                userInputs[2] = String.valueOf(year));
    }

    public void callWebserviceButtonHandler(View view){
        userInputs[0] = titleEditText.getText().toString().trim();
        userInputs[1] = artistEditText.getText().toString().trim();
        //Toast.makeText(this, "userInputs: " + userInputs[0] +
                //", " + userInputs[1] + ", " + userInputs[2], Toast.LENGTH_LONG).show();

        //New a new thread in the following...
    }
}