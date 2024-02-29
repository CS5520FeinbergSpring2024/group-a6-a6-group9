package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

import edu.northeastern.a6_group9_artwork_search.R;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.DBClientListener;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.Message;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.RealtimeDatabaseClient;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.User;

public class UserLoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private RealtimeDatabaseClient databaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        Button loginButton = findViewById(R.id.loginButton);

        databaseClient = new RealtimeDatabaseClient(new DBClientListener() {
            @Override
            public void onUserLoggedIn(User user, String message) {
                if (user != null) {
                    Intent intent = new Intent(UserLoginActivity.this, UserListActivity.class);
                    intent.putExtra("CURRENT_USER_USERNAME", usernameEditText.getText().toString().trim());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserLoginActivity.this, "Error logging in, please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onUserAdded(User user) {
            }

            @Override
            public void onMessageReceived(Message message) {
            }

            @Override
            public void onCountStickersSentFinished(Map<String, Integer> result, String message) {
            }

            @Override
            public void onRetrieveReceivedMessagesFinished(List<Message> result, String message) {
            }
        });

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        if (!username.isEmpty()) {
            databaseClient.loginUser(username);
        } else {
            Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
