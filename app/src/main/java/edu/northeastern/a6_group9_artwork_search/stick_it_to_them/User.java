package edu.northeastern.a6_group9_artwork_search.stick_it_to_them;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String username;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
