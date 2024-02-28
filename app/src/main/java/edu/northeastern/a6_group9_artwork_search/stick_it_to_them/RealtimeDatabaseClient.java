package edu.northeastern.a6_group9_artwork_search.stick_it_to_them;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeDatabaseClient {
    private final String logTag = "RealtimeDatabaseClient";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DBClientListener listener;

    public RealtimeDatabaseClient(@NonNull DBClientListener listener) {
        this.listener = listener;
    }

    public void loginUser(String username) {
        DatabaseReference databaseReference = database.getReference("users");
        databaseReference.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                if (user == null) {
                    user = new User(username);
                    databaseReference.child(username).setValue(user);
                }
                listener.onUserLoggedIn(user, "");
            } else {
                String message = "Error getting data" + task.getException();
                Log.e(logTag, message);
                listener.onUserLoggedIn(null, message);
            }
        });
    }
}
