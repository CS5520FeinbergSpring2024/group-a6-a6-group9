package edu.northeastern.a6_group9_artwork_search;

import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtICClient;
import org.junit.Assert;
import org.junit.Test;

public class ArtICClientTest {
    @Test
    public void test() {
        ArtICClient artICClient = new ArtICClient();
        Assert.assertEquals("https://api.artic.edu/api/v1/", artICClient.getBase());
    }
}
