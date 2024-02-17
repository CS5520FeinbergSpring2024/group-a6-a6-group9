package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;

public class ArtICClient {
    private final String logTag = "ArtICClient";
    private static final String baseUrl = "https://api.artic.edu/api/v1";
    private final String[] artworksFields = new String[]{"id", "title", "thumbnail", "date_display", "artist_display", "dimensions", "artist_id", "category_titles", "image_id"};

    /**
     * List artworks.
     *
     * @param page starting from 1
     * @return may be empty
     */
    public ListResponse listArtwork(int page) {
        JSONObject resp = queryResponse(formatURL("artworks", artworksFields, page, ""));
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
        JSONObject resp = queryResponse(formatURL("artworks", artworksFields, page, formatArtworkQueryParams(fullTextContains, titleContains, completeYearGte, completeYearLte, artistContains)));
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

    public ArrayList<Agent> listAgent(
            ArrayList<String> ids,
            Integer limit,
            ArrayList<String> fields,
            Integer page) throws java.net.MalformedURLException{
        ArrayList<Agent> agents = new ArrayList<>();
        HashMap<String, String> queryParams = new HashMap<>();
        if(ids != null){
            queryParams.put("ids", String.join(",", ids));
        }
        if(limit != null) {
            queryParams.put("limit", limit.toString());
        }
        if(fields != null){
            queryParams.put("fields", String.join(",", fields));
        }
        if(page != null) {
            queryParams.put("page", page.toString());
        }
        JSONObject resp = queryResponse(buildURLWithParams("/agents", queryParams));
        try {
            JSONArray agentsResp = resp.getJSONArray("data");
            for (int i = 0; i < agentsResp.length(); i++) {
                JSONObject cur = agentsResp.getJSONObject(i);
                agents.add(agents.size(), new Agent(
                        cur.getInt("id"),
                        cur.getString("title"),
                        cur.getInt("birth_date"),
                        cur.getInt("death_date"),
                        cur.getString("artist_description"))
                );
            }
        } catch (JSONException e) {
            Log.e(logTag, "JSONException");
        }
        return agents;
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
     * @param resourceName one of the <a href="https://api.artic.edu/docs/#collections-2">artic collections</a>, e.g. artworks, agents
     * @param fields       the fields that need to be kept
     */
    public URL formatURL(String resourceName, String[] fields, int page, String queryParams) {
        StringBuilder urlStringBuilder = new StringBuilder("https://api.artic.edu/api/v1/");
        urlStringBuilder.append(resourceName).append("/search?page=").append(page).append("&fields=").append(String.join(",", fields));
        try {
            urlStringBuilder.append("&params=").append(URLEncoder.encode(queryParams, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(logTag, "UnsupportedEncodingException: " + queryParams);
        }
        URL url = null;
        String urlString = urlStringBuilder.toString();
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(logTag, "MalformedURLException: " + urlString);
        }
        return url;
    }

    /**
     * Builds a url with params.
     *
     * @param resourceName e.g. agents.
     * @param params       url parameters.
     * @return url class.
     */
    public URL buildURLWithParams(
            String resourceName,
            Map<String, String> params
    ) throws java.net.MalformedURLException {
        String url = String.format("%s%s", baseUrl, resourceName);
        if (params == null || params.isEmpty()) {
            return new URL(url);
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return new URL(builder.build().toString());
    }


    public String formatArtworkQueryParams(String fullTextContains, String titleContains, int completeYearGte, int completeYearLte, String artistContains) {
        JSONObject params = new JSONObject();
        try {
            if (!fullTextContains.isEmpty()) {
                params.put("q", fullTextContains);
            }

            JSONObject query = new JSONObject();

            if (!titleContains.isEmpty() || !artistContains.isEmpty()) {
                JSONObject matchQuery = new JSONObject();
                query.put("match", matchQuery);

                if (!titleContains.isEmpty()) {
                    JSONObject titleQuery = new JSONObject();
                    matchQuery.put("title", titleQuery);
                    titleQuery.put("query", titleContains);
                }

                if (!artistContains.isEmpty()) {
                    JSONObject artistQuery = new JSONObject();
                    matchQuery.put("artist_title", artistQuery);
                    artistQuery.put("query", artistContains);
                }
            }

            if (completeYearGte > 0 || completeYearLte > 0) {
                JSONObject rangeQuery = new JSONObject();
                query.put("range", rangeQuery);

                JSONObject completeYearQuery = new JSONObject();
                rangeQuery.put("date_end", completeYearQuery);

                if (completeYearGte > 0) {
                    completeYearQuery.put("gte", completeYearGte);
                }
                if (completeYearLte > 0) {
                    completeYearQuery.put("lte", completeYearLte);
                }
            }

            if (query.length() > 0) {
                params.put("query", query);
            }
        } catch (JSONException e) {
            Log.e(logTag, String.format(Locale.getDefault(), "JSONException, fullTextContains: %s, titleContains: %s, completeYearGt: %d, completeYearLt: %d, artistContains: %s", fullTextContains, titleContains, completeYearGte, completeYearLte, artistContains));
        }
        return params.toString();
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String[] getArtworksFields() {
        return artworksFields;
    }
}
