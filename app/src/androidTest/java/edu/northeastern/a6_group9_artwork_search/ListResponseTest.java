package edu.northeastern.a6_group9_artwork_search;

import android.os.Bundle;
import android.util.Log;
import edu.northeastern.a6_group9_artwork_search.at_your_service.*;
import org.junit.Assert;
import org.junit.Test;

public class ListResponseTest {
    private final String logTag = "ArtICClientTest";

    @Test
    public void testParcelableArtwork() {
        int paginationTotal = 321;
        int artworkId = 123;
        String[] artworkCategories = new String[]{"cool", "good", "nice"};
        Artwork artwork = new Artwork(artworkId, "", "", "", "", "", 0, artworkCategories, "d001c1e1-efa0-a58e-7269-f1f590d2bbe0");
        String listResponseKey = "listResponse";

        Bundle bundle = new Bundle();

        bundle.putParcelable(listResponseKey, new ListResponse(new Pagination(paginationTotal, 0, 0), new Artwork[]{artwork}));
        ListResponse listResponse = bundle.getParcelable(listResponseKey, ListResponse.class);

        Pagination pagination = listResponse.getPagination();
        Assert.assertEquals(paginationTotal, pagination.getTotal());

        artwork = (Artwork) listResponse.getResources()[0];
        Assert.assertEquals(artworkId, artwork.getId());
        Assert.assertArrayEquals(artworkCategories, artwork.getCategories());
        Assert.assertNull(artwork.getImage());

        Log.d(logTag, listResponse.toString());

        // fetch image
        ArtICClient artICClient = new ArtICClient();
        artICClient.fetchArtworkImage(artwork);

        Assert.assertNotNull(artwork.getImage());

        bundle.putParcelable(listResponseKey, new ListResponse(new Pagination(paginationTotal, 0, 0), new Artwork[]{artwork}));
        listResponse = bundle.getParcelable(listResponseKey, ListResponse.class);

        pagination = listResponse.getPagination();
        Assert.assertEquals(paginationTotal, pagination.getTotal());

        artwork = (Artwork) listResponse.getResources()[0];
        Assert.assertEquals(artworkId, artwork.getId());
        Assert.assertArrayEquals(artworkCategories, artwork.getCategories());
        Assert.assertNotNull(artwork.getImage());

        Log.d(logTag, listResponse.toString());
    }

    @Test
    public void testParcelableAgent() {
        int paginationTotal = 321;
        int agentId = 123;
        String agentTitle = "trump";
        String listResponseKey = "listResponse";

        Bundle bundle = new Bundle();

        bundle.putParcelable(listResponseKey, new ListResponse(new Pagination(paginationTotal, 0, 0), new Agent[]{new Agent(agentId, agentTitle, 0, 0, "")}));
        ListResponse listResponse = bundle.getParcelable(listResponseKey, ListResponse.class);

        Pagination pagination = listResponse.getPagination();
        Assert.assertEquals(paginationTotal, pagination.getTotal());

        Agent agent = (Agent) listResponse.getResources()[0];
        Assert.assertEquals(agentId, agent.getId());
        Assert.assertEquals(agentTitle, agent.getTitle());

        Log.d(logTag, listResponse.toString());
    }
}
