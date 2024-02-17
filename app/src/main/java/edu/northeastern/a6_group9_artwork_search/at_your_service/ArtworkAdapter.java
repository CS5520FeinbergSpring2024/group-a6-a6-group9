package edu.northeastern.a6_group9_artwork_search.at_your_service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ViewHolder> {

    private List<Artwork> artworks;

    public ArtworkAdapter(List<Artwork> artworks) {
        this.artworks = artworks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artwork_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artwork artwork = artworks.get(position);
        holder.artworkTitleTextView.setText(artwork.getTitle());
        holder.artworkArtistTextView.setText(artwork.getArtistDisplay());
        holder.artworkYearTextView.setText(artwork.getCompleteYear());

        Glide.with(holder.artworkImageView.getContext())
                .load(artwork.getImageId())
                .placeholder(R.mipmap.ic_no_image)
                .into(holder.artworkImageView);
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView artworkTitleTextView, artworkArtistTextView, artworkYearTextView;
        ImageView artworkImageView;

        public ViewHolder(View view) {
            super(view);
            artworkTitleTextView = view.findViewById(R.id.artworkTitleTextView);
            artworkArtistTextView = view.findViewById(R.id.artworkArtistTextView);
            artworkYearTextView = view.findViewById(R.id.artworkYearTextView);
            artworkImageView = view.findViewById(R.id.artworkImageView);
        }
    }

    public void updateData(List<Artwork> newArtworks) {
        artworks.clear();
        artworks.addAll(newArtworks);
        notifyDataSetChanged();
    }

}
