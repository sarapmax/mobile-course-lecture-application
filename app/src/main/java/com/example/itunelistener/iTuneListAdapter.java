package com.example.itunelistener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class iTuneListAdapter extends BaseAdapter {
    ArrayList<String> mTitles;
    ArrayList<Bitmap> mCovers;
    ArrayList<String> mPreviewUrls;
    Context mContext;

    public iTuneListAdapter(Context context, ArrayList<String> titles, ArrayList<Bitmap> covers, ArrayList<String> previewUrls) {
        mTitles = titles;
        mCovers = covers;
        mPreviewUrls = previewUrls;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCovers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itune_list_item, null);

        TextView textView = itemView.findViewById(R.id.textView);
        textView.setText(mTitles.get(position));

        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(mCovers.get(position));

        return itemView;
    }
}
