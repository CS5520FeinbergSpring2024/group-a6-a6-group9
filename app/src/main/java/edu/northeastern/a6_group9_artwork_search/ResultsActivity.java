package edu.northeastern.a6_group9_artwork_search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtICClient;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Artwork;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtworkAdapter;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ListResponse;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArtICClient artICClient = new ArtICClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_display);

        recyclerView = findViewById(R.id.artworkRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ArtworkAdapter(new ArrayList<>()));

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        int year = intent.getIntExtra("year", 0);

        fetchResults(title, artist, year);
    }

    private void fetchResults(String title, String artist, int year) {
        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        // Mock fetch operation with a delay
        new Thread(() -> {
            // Example call, adjust parameters as needed
            ListResponse listResponse = artICClient.listArtwork(1, title, artist, year, year, artist);
            runOnUiThread(() -> {
                if (listResponse != null) {
                    ((ArtworkAdapter) recyclerView.getAdapter()).updateData(Arrays.asList((Artwork[]) listResponse.getResources()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    private List<Artwork> mockFetchArtworks(String title, String artist, int year) {
        // This method should actually fetch data based on the search parameters
        // The return and implementation are mocked here for illustration
        return new ArrayList<>();
    }
}
