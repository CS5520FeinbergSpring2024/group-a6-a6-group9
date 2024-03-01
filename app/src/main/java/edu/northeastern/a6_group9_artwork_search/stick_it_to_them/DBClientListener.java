package edu.northeastern.a6_group9_artwork_search.stick_it_to_them;

import java.util.List;
import java.util.Map;

import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message.Message;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user.User;

public interface DBClientListener {
    /**
     * If succeeded, user != null && message == null. Otherwise, user == null && message != null
     */
    void onUserLoggedIn(User user, String message);

    /**
     *  Will be triggered in two cases:
     *  1. Will be triggered multiple times when listener attaches. An existing user in db will be provided.
     *  2. Will be triggered when a new user signs up.
     * @param user the user in db
     */
    void onUserAdded(User user);

    /**
     *  Will be triggered when a new message is sent to or from the login user.
     * @param message the message in db
     */
    void onMessageReceived(Message message);
    /**
     * If succeeded, result != null && message == null. Otherwise, result == null && message != null
     */
    void onCountStickersSentFinished(Map<String, Integer> result, String message);
    /**
     * If succeeded, result != null && message == null. Otherwise, result == null && message != null
     */
    void onRetrieveReceivedMessagesFinished(List<Message> result, String message);
}
