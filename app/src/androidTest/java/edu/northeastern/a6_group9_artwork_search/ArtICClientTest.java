package edu.northeastern.a6_group9_artwork_search;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtICClient;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Artwork;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ListResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ArtICClientTest {
    private final String logTag = "ArtICClientTest";


    @Test
    public void testBuildURLWithParams() {
        String expectedUrl = "https://api.artic.edu/api/v1/test_resource?field=value";
        HashMap<String, String> testParams = new HashMap<>();
        testParams.put("field", "value");
        ArtICClient artICClient = new ArtICClient();
        Assert.assertEquals(
                artICClient.buildURLWithParams(
                        "test_resource", testParams).toString(), expectedUrl);
    }

    @Test
    public void testListAgent() {
        // The JSON string you want to return;
        ArtICClient artICClient = new ArtICClient();
        ListResponse result = artICClient.listAgent(1);
        Assert.assertTrue(result.getResources().length > 0);
    }

    @Test
    public void testListArtwork() {
        ArtICClient artICClient = new ArtICClient();
        ListResponse listArtworkResponse = artICClient.listArtwork(2);

        Assert.assertNotEquals(0, listArtworkResponse.getResources().length);

        Artwork artwork = (Artwork) listArtworkResponse.getResources()[0];
        Assert.assertNotEquals(0, artwork.getTitle().length());

        Log.d(logTag, listArtworkResponse.toString());
    }

    @Test
    public void testFetchArtworkArtist() {
        ArtICClient artICClient = new ArtICClient();
        ListResponse listArtworkResponse = artICClient.listArtwork(1);
        Artwork artwork = (Artwork) listArtworkResponse.getResources()[0];
        Assert.assertNull(artwork.getArtist());
        artICClient.fetchArtworkArtist(artwork);
        Assert.assertNotNull(artwork.getArtist());
    }


    @Test
    public void testArtworkFormat() {
        ArtICClient artICClient = new ArtICClient();
        Map<String, String> params = new HashMap<>();
        params.put("fields",  String.join(",", artICClient.getArtworksFields()));
        params.put("page", String.valueOf(1));
        String searchParams;
        searchParams = artICClient.formatArtworkSearchParams("full", "", 0, 0, "");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("", "title", 0, 0, "");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("", "", 0, 0, "artist");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("", "", 1000, 0, "");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("", "", 0, 2000, "");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("full", "", 1000, 2000, "");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("", "", 1000, 2000, "artist");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
        searchParams = artICClient.formatArtworkSearchParams("full", "title", 1000, 2000, "artist");
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("artworks", params)));
    }

    @Test
    public void testAgentFormat() {
        ArtICClient artICClient = new ArtICClient();
        Map<String, String> params = new HashMap<>();
        params.put("fields",  String.join(",", artICClient.getAgentsFields()));
        params.put("page", String.valueOf(1));
        String searchParams;
        searchParams = artICClient.formatAgentSearchParams(96937);
        params.put("params", searchParams);
        Log.d(logTag, String.format(Locale.getDefault(), "searchParams: %s, url: %s", searchParams, artICClient.buildURLWithParams("agents", params)));
    }
}