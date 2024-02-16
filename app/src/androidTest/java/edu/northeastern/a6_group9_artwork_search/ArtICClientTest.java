package edu.northeastern.a6_group9_artwork_search;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtICClient;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Artwork;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ListResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ArtICClientTest {
    private final String logTag = "ArtICClientTest";
    @Test
    public void testListArtwork() {
        ArtICClient artICClient = new ArtICClient();
        ListResponse listArtworkResponse = artICClient.listArtwork(2);

        Assert.assertNotEquals(0, listArtworkResponse.getResources().length);

        Artwork artwork = (Artwork) listArtworkResponse.getResources()[0];
        Assert.assertNotEquals(0, artwork.getTitle().length());

        Log.d(logTag, listArtworkResponse.toString());
    }
}