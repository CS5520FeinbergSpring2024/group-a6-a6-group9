package edu.northeastern.a6_group9_artwork_search;

import android.os.Bundle;
import android.util.Log;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Artwork;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ListResponse;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Pagination;
import org.junit.Assert;
import org.junit.Test;

public class ListResponseTest {
    private final String logTag = "ArtICClientTest";

    @Test
    public void testParcelableArtwork() {
        int paginationTotal = 321;
        String resourceType = Artwork.class.getSimpleName();
        int artworkId = 123;
        String[] artworkCategories = new String[]{"cool", "good", "nice"};
        String listResponseKey = "listResponse";

        Bundle bundle = new Bundle();

        bundle.putParcelable(listResponseKey, new ListResponse(new Pagination(paginationTotal, 0, 0), resourceType, new Artwork[]{new Artwork(artworkId, "", "", "", "", "", 0, artworkCategories, "")}));
        ListResponse listResponse = bundle.getParcelable(listResponseKey, ListResponse.class);

        Pagination pagination = listResponse.getPagination();
        Assert.assertEquals(paginationTotal, pagination.getTotal());

        Assert.assertEquals(resourceType, listResponse.getResourceType());

        Artwork artwork = (Artwork) listResponse.getResources()[0];
        Assert.assertEquals(artworkId, artwork.getId());
        Assert.assertArrayEquals(artworkCategories, artwork.getCategories());

        Log.d(logTag, listResponse.toString());
    }
}
