package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;
    private String currentUsername;
    private final int VIEW_TYPE_SENDER = 1;
    private final int VIEW_TYPE_RECEIVER = 2;

    public MessageAdapter(List<Message> messageList, String currentUsername) {
        this.messageList = messageList;
        this.currentUsername = currentUsername;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (TextUtils.equals(message.getSenderUsername(), currentUsername)) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_RECEIVER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENDER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sender, parent, false);
            return new SenderViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_receiver, parent, false);
            return new ReceiverViewHolder(view);
        } else {
            view = new View(parent.getContext());
            view.setVisibility(View.GONE);
            return new RecyclerView.ViewHolder(view) {};
    }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderHolder = (SenderViewHolder) holder;
            int resId = holder.itemView.getContext().getResources().getIdentifier(message.getStickerId(), "drawable", holder.itemView.getContext().getPackageName());
            senderHolder.senderStickerImageView.setImageResource(resId);
            senderHolder.senderUsernameTextView.setText(currentUsername);
        } else if (holder instanceof ReceiverViewHolder) {
            ReceiverViewHolder receiverHolder = (ReceiverViewHolder) holder;
            int resId = holder.itemView.getContext().getResources().getIdentifier(message.getStickerId(), "drawable", holder.itemView.getContext().getPackageName());
            receiverHolder.receiverStickerImageView.setImageResource(resId);
            receiverHolder.receiverUsernameTextView.setText(message.getSenderUsername());

        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }


    static class SenderViewHolder extends RecyclerView.ViewHolder {
        ImageView senderStickerImageView;
        TextView senderUsernameTextView;
        SenderViewHolder(View itemView) {
            super(itemView);
            senderStickerImageView = itemView.findViewById(R.id.senderStickerImageView);
            senderUsernameTextView = itemView.findViewById(R.id.senderUsername);
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ImageView receiverStickerImageView;
        TextView receiverUsernameTextView;
        ReceiverViewHolder(View itemView) {
            super(itemView);
            receiverStickerImageView = itemView.findViewById(R.id.receiverStickerImageView);
            receiverUsernameTextView = itemView.findViewById(R.id.receiverUsername);
        }
    }
}
