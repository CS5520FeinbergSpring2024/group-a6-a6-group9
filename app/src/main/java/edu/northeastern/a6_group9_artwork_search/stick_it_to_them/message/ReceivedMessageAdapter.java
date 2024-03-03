package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;

public class ReceivedMessageAdapter extends RecyclerView.Adapter<ReceivedMessageAdapter.MyViewHolder> {

    Context context;
    List<Message> receivedMessageList;

    public ReceivedMessageAdapter(Context context, List<Message> list) {
        this.context = context;
        this.receivedMessageList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.received_message_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = receivedMessageList.get(position);

        int resId = holder.itemView.getContext().getResources().getIdentifier(message.getStickerId(), "drawable", holder.itemView.getContext().getPackageName());
        holder.sticker.setImageResource(resId);
        holder.sender.setText("Sender: " + message.getSenderUsername());
        holder.sentDate.setText(message.getSendTime().toString());
    }

    @Override
    public int getItemCount() {
        return receivedMessageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView sticker;
        TextView sender, sentDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sticker = (ImageView) itemView.findViewById(R.id.received_sticker_iv);
            sender = (TextView) itemView.findViewById(R.id.sender_username_tv);
            sentDate = (TextView) itemView.findViewById(R.id.sent_date_tv);
        }
    }
}
