package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.os.Parcel;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Agent implements Resource {
    private final int id;
    private final String title;
    private final int birthDate;
    private final int deathDate;
    private final String description;

    public Agent(
            int id,
            String title,
            int birthDate,
            int deathDate,
            String description
    ) {
        this.id = id;
        this.title = title;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.description = description;
    }

    protected Agent(Parcel in) {
        id = in.readInt();
        title = in.readString();
        birthDate = in.readInt();
        deathDate = in.readInt();
        description = in.readString();
    }

    public static final Creator<Agent> CREATOR = new Creator<Agent>() {

        @Override
        public Agent createFromParcel(Parcel in) {
            return new Agent(in);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };

    /**
     * @return internal id of the person or organization
     */
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    /**
     * @return text to display when image load failed
     */
    public int getBirthDate() {
        return birthDate;
    }

    public int getDeathDate() {
        return deathDate;
    }

    public String getDescription() {
        return description;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "id: %d, title: %s, birthDate: %d, deathDate: %d, artistDescription: %s", id, title, birthDate, deathDate, description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(birthDate);
        dest.writeInt(deathDate);
        dest.writeString(description);
    }
}

