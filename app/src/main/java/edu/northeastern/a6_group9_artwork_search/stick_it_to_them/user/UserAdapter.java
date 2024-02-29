package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;
import edu.northeastern.a6_group9_artwork_search.stick_it_to_them.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private LayoutInflater inflater;
    private List<User> userList;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClicked(User user);
    }

    public UserAdapter(Context context, List<User> userList, OnUserClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.userList = userList;
        this.listener = listener;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        public UserViewHolder(@NonNull View itemView, final OnUserClickListener listener) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);

            itemView.setOnClickListener(view -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onUserClicked((User) view.getTag());
                }
            });
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.itemView.setTag(user);
        holder.userNameTextView.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
