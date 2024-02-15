package edu.northeastern.a6_group9_artwork_search;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtICClient;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Artwork;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ArtICClientTest {
    @Test
    public void testListArtwork() {
        ArtICClient artICClient = new ArtICClient();
        Artwork[] artworks = artICClient.listArtwork(1);
        Assert.assertNotEquals(0, artworks.length);
        Assert.assertNotEquals(0, artworks[0].getTitle().length());
    }
}