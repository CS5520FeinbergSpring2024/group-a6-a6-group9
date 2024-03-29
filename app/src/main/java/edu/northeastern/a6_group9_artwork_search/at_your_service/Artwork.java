package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.graphics.Bitmap;
import android.os.Parcel;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Artwork implements Resource {
    private final int id;
    private final String title;
    private final String altText;
    private final String completeYear;
    private final String artistDisplay;
    private final String dimensions;
    private final int artistId;
    private final String[] categories;
    private final String imageId;
    private Bitmap image;
    private Agent artist;

    public Artwork(int id,  String title, String altText, String completeYear, String artistDisplay, String dimensions, int artistId, String[] categories, String imageId) {
        this.id = id;
        this.title = title;
        this.altText = altText;
        this.completeYear = completeYear;
        this.artistDisplay = artistDisplay;
        this.dimensions = dimensions;
        this.artistId = artistId;
        this.categories = categories;
        this.imageId = imageId;
        image = null;
        artist = null;
    }

    protected Artwork(Parcel in) {
        id = in.readInt();
        title = in.readString();
        altText = in.readString();
        completeYear = in.readString();
        artistDisplay = in.readString();
        dimensions = in.readString();
        artistId = in.readInt();
        categories = in.createStringArray();
        imageId = in.readString();
        image = in.readTypedObject(Bitmap.CREATOR);
        artist = in.readTypedObject(Agent.CREATOR);
    }

    public static final Creator<Artwork> CREATOR = new Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            return new Artwork(in);
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };

    /**
     *
     * @return internal id of the artwork
     */
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    /**
     *
     * @return text to display when image load failed, may be empty
     */
    public String getAltText() {
        return altText;
    }

    /**
     *
     * @return may be "unknown
     */
    public String getCompleteYear() {
        return completeYear;
    }

    /**
     *
     * @return may be "unknown" if there is no artist associated
     */
    public String getArtistDisplay() {
        return artistDisplay;
    }

    public String getDimensions() {
        return dimensions;
    }

    /**
     *
     * @return may be 0 if there is no artist associated
     */
    public int getArtistId() {
        return artistId;
    }

    /**
     *
     * @return may be empty if there is no categories associated
     */
    public String[] getCategories() {
        return categories;
    }

    /**
     *
     * @return used for fetching image, may be 0 if there is no image associated
     */
    public String getImageId() {
        return imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Agent getArtist() {
        return artist;
    }

    public void setArtist(Agent artist) {
        this.artist = artist;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "id: %d, title: %s, altText: %s, completeYear: %s, artistDisplay: %s, dimensions: %s, artistId: %d, categories: [%s], imageId: %s, artist: %s", id, title, altText, completeYear, artistDisplay, dimensions, artistId, String.join(",", categories), imageId, artist);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(altText);
        dest.writeString(completeYear);
        dest.writeString(artistDisplay);
        dest.writeString(dimensions);
        dest.writeInt(artistId);
        dest.writeStringArray(categories);
        dest.writeString(imageId);
        dest.writeTypedObject(image, flags);
        dest.writeTypedObject(artist, flags);
    }
}
