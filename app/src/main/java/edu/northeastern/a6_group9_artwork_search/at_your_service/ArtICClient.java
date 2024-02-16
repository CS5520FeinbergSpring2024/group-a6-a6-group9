package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class ArtICClient {
    private final String logTag = "ArtICClient";
    private final String[] artworksFields = new String[]{"id", "title", "thumbnail", "date_display", "artist_display", "dimensions", "artist_id", "category_titles", "image_id"};

    /**
     * List artworks.
     *
     * @param page starting from 1
     *
     */
    public ListResponse listArtwork(int page) {
        Artwork[] artworks = new Artwork[0];
        Pagination pagination = null;
        JSONObject resp = queryResponse(formatURL("artworks", artworksFields, page, ""));
        try {
            JSONObject paginationJsonObject = resp.getJSONObject("pagination");
            pagination = new Pagination(paginationJsonObject.getInt("total"), paginationJsonObject.getInt("total_pages"), paginationJsonObject.getInt("current_page"));
            JSONArray artworksResp = resp.getJSONArray("data");
            artworks = new Artwork[artworksResp.length()];

            for (int i = 0; i < artworksResp.length(); i++) {
                JSONObject cur = artworksResp.getJSONObject(i);

                JSONArray categoriesJsonArray = cur.optJSONArray("category_titles");
                String[] categories;
                if (categoriesJsonArray != null) {
                    categories = new String[categoriesJsonArray.length()];
                    for (int j = 0; j < categoriesJsonArray.length(); j++) {
                        categories[j] = categoriesJsonArray.getString(j);
                    }
                } else {
                    categories = new String[0];
                }

                String altText = "";
                JSONObject thumbnailJsonObject = cur.optJSONObject("thumbnail");
                if (thumbnailJsonObject != null) {
                    altText = thumbnailJsonObject.optString("alt_text");
                }

                artworks[i] = new Artwork(cur.getInt("id"), cur.optString("title", "Unnamed"), altText, cur.optString("date_display", "unknown"), cur.optString("artist_display", "unknown"), cur.optString("dimensions", "unknown"), cur.optInt("artist_id"), categories, cur.optString("image_id"));
            }
        } catch (JSONException e) {
            Log.e(logTag, "JSONException");
        }
        return new ListResponse(pagination, artworks);
    }

    /**
     *
     * @param page page number
     * @param fullTextContains will be used in a full text search on all metadata
     * @param titleContains will be searched for as a substring in title
     * @param completeYearGt will be compared with the complete year, keep whose complete year is greater than the given number
     * @param completeYearLt will be compared with the complete year, keep whose complete year is lower than the given number
     * @param artistContains will be searched for as a substring in artist
     */
    public ListResponse listArtwork(int page, String fullTextContains, String titleContains, int completeYearGt, int completeYearLt, String artistContains) {
        return null;
    }

    private JSONObject queryResponse(URL url) {
        JSONObject resp = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            final String respStr = convertStreamToString(inputStream);
            resp = new JSONObject(respStr);
        } catch (IOException e) {
            Log.e(logTag, "IOException");
        } catch (JSONException e) {
            Log.e(logTag, "JSONException");
        }
        return resp;
    }

    /**
     *
     * @param resourceName one of the <a href="https://api.artic.edu/docs/#collections-2">artic collections</a>, e.g. artworks, agents
     * @param fields the fields that need to be kept
     * @param page page number
     * @param fullTextQuery the full text search pattern that will be used in q, can be empty
     */
    private URL formatURL(String resourceName, String[] fields, int page, String fullTextQuery) {
        StringBuilder urlStringBuilder = new StringBuilder("https://api.artic.edu/api/v1/");
        urlStringBuilder.append(resourceName).append("/search?page=").append(page).append("&fields=").append(String.join(",", fields));
        try {
            if (fullTextQuery != null && !fullTextQuery.isEmpty()) {
                urlStringBuilder.append("&params=").append(URLEncoder.encode(String.format("{\"q\":\"%s\"}", fullTextQuery), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(logTag, "UnsupportedEncodingException: " + fullTextQuery);
        }
        URL url = null;
        String urlString = urlStringBuilder.toString();
        Log.d(logTag, "urlString: " + urlString);
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(logTag, "MalformedURLException: " + urlString);
        }
        return url;
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
