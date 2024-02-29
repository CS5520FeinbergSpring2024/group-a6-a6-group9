package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class StickerAdapter extends BaseAdapter {
    private Context context;
    private int[] stickerIds;

    public StickerAdapter(Context context, int[] stickerIds) {
        this.context = context;
        this.stickerIds = stickerIds;
    }

    @Override
    public int getCount() {
        return stickerIds.length;
    }

    @Override
    public Object getItem(int position) {
        return stickerIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(stickerIds[position]);
        return imageView;
    }
}
