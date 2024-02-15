package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ArtICClient {
    private final String logTag = "ArtICClient";

    /**
     * List artworks.
     *
     * @param page starting from 1
     *
     */
    public Artwork[] listArtwork(int page) {
        Artwork[] artworks;
        JSONObject resp = queryResponse(formatURL("artworks", page));
        try {
            JSONArray artworksResp = resp.getJSONArray("data");
            artworks = new Artwork[artworksResp.length()];
            for (int i = 0; i < artworksResp.length(); i++) {
                JSONObject cur = artworksResp.getJSONObject(i);
                JSONArray categoriesJsonArray = cur.getJSONArray("category_titles");
                String[] categories = new String[categoriesJsonArray.length()];
                for (int j = 0; j < categoriesJsonArray.length(); j++) {
                    categories[j] = categoriesJsonArray.getString(j);
                }
                artworks[i] = new Artwork(cur.getInt("id"), cur.getString("title"), cur.getJSONObject("thumbnail").getString("alt_text"), cur.getInt("date_end"), cur.getString("artist_display"), cur.getString("dimensions"), cur.getInt("artist_id"), categories, cur.getString("image_id"));
            }
        } catch (JSONException e) {
            Log.e(logTag, "JSONException");
            artworks = new Artwork[0];
        }
        return artworks;
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

    private URL formatURL(String resourceName, int page) {
        String base = "https://api.artic.edu/api/v1/";
        URL url = null;
        try {
            url = new URL(base + resourceName + "?page=" + page);
        } catch (MalformedURLException e) {
            Log.e(logTag, "MalformedURLException");
        }
        return url;
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
