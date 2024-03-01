package edu.northeastern.a6_group9_artwork_search;

import android.util.Log;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.DBClientListener;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.Message;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.RealtimeDatabaseClient;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.User;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class RealtimeDatabaseClientTest {
    private final String logTag = "RealtimeDatabaseClientTest";
    private final DBClientListener listener = new DBClientListener() {
        @Override
        public void onUserLoggedIn(User user, String message) {
            String funcLogTag = logTag + "_onUserLoggedIn";
            if (user != null) {
                Log.d(funcLogTag, user.getUsername());
            } else {
                Log.d(funcLogTag, message);
            }
        }

        @Override
        public void onUserAdded(User user) {
            String funcLogTag = logTag + "_onUserAdded";
            Log.d(funcLogTag, user.getUsername());
        }

        @Override
        public void onMessageReceived(Message message) {
            String funcLogTag = logTag + "_onMessageReceived";
            Log.d(funcLogTag, message.getStickerId());
        }

        @Override
        public void onCountStickersSentFinished(Map<String, Integer> result, String message) {
            String funcLogTag = logTag + "_onCountStickersSentFinished";
            if (result != null) {
                Log.d(funcLogTag, result.toString());
            } else {
                Log.d(funcLogTag, message);
            }
        }

        @Override
        public void onRetrieveReceivedMessagesFinished(List<Message> result, String message) {
            String funcLogTag = logTag + "_onRetrieveReceivedMessagesFinished";
            if (result != null) {
                Log.d(funcLogTag, result.toString());
            } else {
                Log.d(funcLogTag, message);
            }
        }
    };

    private final RealtimeDatabaseClient realtimeDatabaseClient = new RealtimeDatabaseClient(listener);

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Log.e(logTag, e.toString());
        }
    }

    @Test
    public void testLaunch() {
        sleep();
    }

    @Test
    public void testLoginUser() {
        realtimeDatabaseClient.loginUser("hhh");
        realtimeDatabaseClient.loginUser("hhh1");
        sleep();
    }

    @Test
    public void testSendMessage() {
        realtimeDatabaseClient.sendMessage(new Message("hhh", "hhh1", "2"));
        sleep();
    }

    @Test
    public void testCountStickersSent() {
        realtimeDatabaseClient.countStickersSent(new User("hhh2"));
        sleep();
    }

}
