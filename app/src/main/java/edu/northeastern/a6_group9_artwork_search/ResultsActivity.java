package edu.northeastern.a6_group9_artwork_search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtICClient;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Artwork;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ArtworkAdapter;
import edu.northeastern.a6_group9_artwork_search.at_your_service.ListResponse;
import edu.northeastern.a6_group9_artwork_search.at_your_service.Pagination;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArtICClient artICClient = new ArtICClient();

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_display);

        recyclerView = findViewById(R.id.artworkRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        FloatingActionButton fabBack = findViewById(R.id.backFab);

        fabBack.setOnClickListener(view -> {
            finish();
        });

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ArtworkAdapter(new ArrayList<>()));

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        int year = intent.getIntExtra("year", 0);

        fetchResults(title, artist, year);
        setupScrollListener(title, artist, year);
    }

    private void fetchResults(String title, String artist, int year) {
        if (isLoading || isLastPage) {
            return;
        }
        isLoading = true;
        // Show loading indicator only for the first page
        if (currentPage == 1) {
            progressBar.setVisibility(View.VISIBLE);
        }

        new Thread(() -> {
            ListResponse listResponse = artICClient.listArtwork(currentPage, title, title, year, year, artist);
            runOnUiThread(() -> {
                isLoading = false;
                if (currentPage == 1) {
                    progressBar.setVisibility(View.GONE);
                }
                if (listResponse != null) {
                    if (currentPage > 1) {
                        ((ArtworkAdapter) recyclerView.getAdapter()).addData(Arrays.asList((Artwork[]) listResponse.getResources()));
                    } else {
                        ((ArtworkAdapter) recyclerView.getAdapter()).updateData(Arrays.asList((Artwork[]) listResponse.getResources()));
                    }

                    Pagination pagination = listResponse.getPagination();
                    if (pagination != null) {
                        isLastPage = currentPage >= pagination.getTotalPages();
                    }
                }
            });
        }).start();
    }

    private void setupScrollListener(String title, String artist, int year) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if (lastVisibleItemPosition + 1 >= totalItemCount) {
                        currentPage++; // Increment page number
                        fetchResults(title, artist, year); // Fetch next page
                    }
                }
            }
        });
    }
}
