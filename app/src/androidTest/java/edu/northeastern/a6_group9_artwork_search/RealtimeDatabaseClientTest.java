package edu.northeastern.a6_group9_artwork_search;

import android.util.Log;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.DBClientListener;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.RealtimeDatabaseClient;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.User;
import org.junit.Test;

public class RealtimeDatabaseClientTest {
    private final String logTag = "RealtimeDatabaseClientTest";
    private final DBClientListener listener = new DBClientListener() {
        @Override
        public void onUserLoggedIn(User user, String message) {
            Log.d(logTag, message);
            Log.d(logTag, user.getUsername());
        }
    };

    @Test
    public void testLoginUser() {
        RealtimeDatabaseClient realtimeDatabaseClient = new RealtimeDatabaseClient(listener);
        realtimeDatabaseClient.loginUser("hhh");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Log.e(logTag, e.toString());
        }
    }
}
