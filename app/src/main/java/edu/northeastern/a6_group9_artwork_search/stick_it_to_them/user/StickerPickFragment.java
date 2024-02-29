package edu.northeastern.a6_group9_artwork_search.stick_it_to_them.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.a6_group9_artwork_search.R;

public class StickerPickFragment extends DialogFragment {
    private GridView gridViewStickers;

    public interface OnStickerSelectedListener {
        void onStickerSelected(int stickerResId);
    }

    private OnStickerSelectedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnStickerSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement OnStickerSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_pick_frag, container, false);
        gridViewStickers = view.findViewById(R.id.gridViewStickers);

        List<Integer> stickerIdsList = new ArrayList<>();
        Field[] drawables = R.drawable.class.getFields();
        for (Field field : drawables) {
            try {
                if (field.getName().startsWith("frog_")) {
                    int resId = field.getInt(null);
                    stickerIdsList.add(resId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int[] stickerIds = new int[stickerIdsList.size()];
        for (int i = 0; i < stickerIdsList.size(); i++) {
            stickerIds[i] = stickerIdsList.get(i);
        }
        gridViewStickers.setAdapter(new StickerAdapter(getActivity(), stickerIds));

        gridViewStickers.setOnItemClickListener((parent, view1, position, id) -> {
            if (listener != null) {
                listener.onStickerSelected(stickerIds[position]);
            }
        });

        return view;
    }
}