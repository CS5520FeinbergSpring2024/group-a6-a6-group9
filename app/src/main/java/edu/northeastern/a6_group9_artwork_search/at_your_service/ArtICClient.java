package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.Map;

public class ArtICClient {
    private final String logTag = "ArtICClient";
    private final String[] artworksFields = new String[]{"id", "title", "thumbnail", "date_display", "artist_display", "dimensions", "artist_id", "category_titles", "image_id"};
    private final String[] agentsFields = new String[]{"id", "title", "birth_date", "death_date", "description"};

    /**
     * List artworks.
     *
     * @param page starting from 1
     * @return may be empty
     */
    public ListResponse listArtwork(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("fields", String.join(",", artworksFields));
        params.put("page", String.valueOf(page));
        JSONObject resp = queryJsonResponse(buildURLWithParams("artworks", params));
        return new ListResponse(getPagination(resp), getArtworks(resp));
    }

    /**
     * @param page             page number
     * @param fullTextContains will be used in a full text search on all metadata, case in-sensitive
     * @param titleContains    will be searched for as a substring in title, case in-sensitive
     * @param completeYearGte  will be compared with the complete year, keep whose complete year is greater than or equal to the given number
     * @param completeYearLte  will be compared with the complete year, keep whose complete year is lower than or equal to the given number
     * @param artistContains   will be searched for as a substring in artist, case in-sensitive
     * @return may be empty
     */
    public ListResponse listArtwork(int page, String fullTextContains, String titleContains, int completeYearGte, int completeYearLte, String artistContains) {
        Map<String, String> params = new HashMap<>();
        params.put("fields", String.join(",", artworksFields));
        params.put("page", String.valueOf(page));
        params.put("params", formatArtworkSearchParams(fullTextContains, titleContains, completeYearGte, completeYearLte, artistContains));
        JSONObject resp = queryJsonResponse(buildURLWithParams("artworks", params));
        return new ListResponse(getPagination(resp), getArtworks(resp));
    }

    private Pagination getPagination(JSONObject obj) {
        Pagination pagination = null;
        try {
            JSONObject paginationJsonObject = obj.getJSONObject("pagination");
            pagination = new Pagination(paginationJsonObject.getInt("total"), paginationJsonObject.getInt("total_pages"), paginationJsonObject.getInt("current_page"));
        } catch (JSONException e) {
            Log.e(logTag, "cannot find pagination section");
        }
        return pagination;
    }

    private Artwork[] getArtworks(JSONObject obj) {
        Artwork[] artworks = new Artwork[0];
        try {
            JSONArray artworksResp = obj.getJSONArray("data");
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
        return artworks;
    }

    private Agent[] getAgents(JSONObject obj) {
        Agent[] agents = new Agent[0];
        try {
            JSONArray agentsResp = obj.getJSONArray("data");
            agents = new Agent[agentsResp.length()];

            for (int i = 0; i < agentsResp.length(); i++) {
                JSONObject cur = agentsResp.getJSONObject(i);

                agents[i] = new Agent(
                        cur.getInt("id"),
                        cur.optString("title", "No title"),
                        cur.optInt("birth_date", -1),
                        cur.optInt("death_date", -1),
                        cur.optString("description", "No description"));
            }
        } catch (JSONException e) {
            Log.e(logTag, "JSONException");
        }
        return agents;
    }

    public ListResponse listAgent(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("fields", String.join(",", agentsFields));
        params.put("page", String.valueOf(page));
        JSONObject resp = queryJsonResponse(buildURLWithParams("agents", params));
        return new ListResponse(getPagination(resp), getAgents(resp));
    }

    public ListResponse listAgent(int page, int id) {
        Map<String, String> params = new HashMap<>();
        params.put("fields", String.join(",", agentsFields));
        params.put("page", String.valueOf(page));
        params.put("params", formatAgentSearchParams(id));
        JSONObject resp = queryJsonResponse(buildURLWithParams("agents", params));
        return new ListResponse(getPagination(resp), getAgents(resp));
    }

    private JSONObject queryJsonResponse(URL url) {
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
     * Builds an url with params.
     *
     * @param resourceName e.g. agents.
     * @param params       url parameters.
     * @return url class.
     */
    public URL buildURLWithParams(
            String resourceName,
            Map<String, String> params
    ) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.artic.edu")
                .appendPath("api")
                .appendPath("v1")
                .appendPath(resourceName)
                .appendPath("search");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        try {
            return new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            Log.e(logTag, "MalformedURLException");
            return null;
        }
    }

    public String formatArtworkSearchParams(String fullTextContains, String titleContains, int completeYearGte, int completeYearLte, String artistContains) {
        JSONObject params = new JSONObject();
        try {
            if (!fullTextContains.isEmpty()) {
                params.put("q", fullTextContains);
            }

            JSONObject query = new JSONObject();
            JSONObject boolQuery = new JSONObject();
            query.put("bool", boolQuery);
            JSONArray mustQuery = new JSONArray();
            boolQuery.put("must", mustQuery);

            if (!titleContains.isEmpty() || !artistContains.isEmpty()) {
                if (!titleContains.isEmpty()) {
                    JSONObject subMustQuery = new JSONObject();
                    mustQuery.put(subMustQuery);
                    JSONObject matchQuery = new JSONObject();
                    subMustQuery.put("match", matchQuery);
                    JSONObject titleQuery = new JSONObject();
                    matchQuery.put("title", titleQuery);
                    titleQuery.put("query", titleContains);
                }

                if (!artistContains.isEmpty()) {
                    JSONObject subMustQuery = new JSONObject();
                    mustQuery.put(subMustQuery);
                    JSONObject matchQuery = new JSONObject();
                    subMustQuery.put("match", matchQuery);
                    JSONObject artistQuery = new JSONObject();
                    matchQuery.put("artist_title", artistQuery);
                    artistQuery.put("query", artistContains);
                }
            }

            if (completeYearGte > 0 || completeYearLte > 0) {
                JSONObject subMustQuery = new JSONObject();
                mustQuery.put(subMustQuery);
                JSONObject rangeQuery = new JSONObject();
                subMustQuery.put("range", rangeQuery);

                JSONObject completeYearQuery = new JSONObject();
                rangeQuery.put("date_end", completeYearQuery);

                if (completeYearGte > 0) {
                    completeYearQuery.put("gte", completeYearGte);
                }
                if (completeYearLte > 0) {
                    completeYearQuery.put("lte", completeYearLte);
                }
            }

            if (mustQuery.length() > 0) {
                params.put("query", query);
            }
        } catch (JSONException e) {
            Log.e(logTag, String.format(Locale.getDefault(), "JSONException, fullTextContains: %s, titleContains: %s, completeYearGt: %d, completeYearLt: %d, artistContains: %s", fullTextContains, titleContains, completeYearGte, completeYearLte, artistContains));
        }
        return params.toString();
    }

    public String formatAgentSearchParams(int id) {
        JSONObject params = new JSONObject();
        try {
            JSONObject query = new JSONObject();
            params.put("query", query);
            JSONObject termQuery = new JSONObject();
            query.put("term", termQuery);
            JSONObject idQuery = new JSONObject();
            termQuery.put("id", idQuery);
            idQuery.put("value", id);
        } catch (JSONException e) {
            Log.e(logTag, "JSONException, id: " + id);
        }
        return params.toString();
    }

    public Bitmap fetchArtworkImage(Artwork artwork) {
        Bitmap image = null;
        String url = "https://www.artic.edu/iiif/2/" + artwork.getImageId() + "/full/843,/0/default.jpg";
        try {
            InputStream in = new URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            Log.e(logTag, "MalformedURLException, url: " + url);
        } catch (IOException e) {
            Log.e(logTag, "IOException");
        }
        artwork.setImage(image);
        return image;
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String[] getArtworksFields() {
        return artworksFields;
    }

    public String[] getAgentsFields() {
        return agentsFields;
    }
}
