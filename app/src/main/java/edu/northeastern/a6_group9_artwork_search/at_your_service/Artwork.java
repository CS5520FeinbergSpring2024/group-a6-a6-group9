package edu.northeastern.a6_group9_artwork_search.at_your_service;

public class Artwork {
    private final int id;
    private final String title;
    private final String altText;
    private final int completeYear;
    private final String artistDisplay;
    private final String dimensions;
    private final int artistId;
    private final String[] categories;
    private final String imageId;

    public Artwork(int id,  String title, String altText, int completeYear, String artistDisplay, String dimensions, int artistId, String[] categories, String imageId) {
        this.id = id;
        this.title = title;
        this.altText = altText;
        this.completeYear = completeYear;
        this.artistDisplay = artistDisplay;
        this.dimensions = dimensions;
        this.artistId = artistId;
        this.categories = categories;
        this.imageId = imageId;
    }

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
     * @return text to display when image load failed
     */
    public String getAltText() {
        return altText;
    }

    public int getCompleteYear() {
        return completeYear;
    }

    public String getArtistDisplay() {
        return artistDisplay;
    }

    public String getDimensions() {
        return dimensions;
    }

    public int getArtistId() {
        return artistId;
    }

    public String[] getCategories() {
        return categories;
    }

    /**
     *
     * @return used for fetching image
     */
    public String getImageId() {
        return imageId;
    }
}
