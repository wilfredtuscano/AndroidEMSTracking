package com.example.wtuscano.emstracking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.activity.AdminMainActivity;
import com.example.wtuscano.emstracking.activity.DriverMainActivity;
import com.example.wtuscano.emstracking.activity.EventImageViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridEventImagesAdapter extends BaseAdapter {

    ArrayList<String> mDataset = new ArrayList<>();
    Context mContext;
    private static LayoutInflater inflater = null;

    public GridEventImagesAdapter(AdminMainActivity mainActivity, ArrayList<String> imageURLs) {
        mDataset = imageURLs;
        mContext = mainActivity;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public GridEventImagesAdapter(DriverMainActivity mainActivity, ArrayList<String> imageURLs) {
        mDataset = imageURLs;
        mContext = mainActivity;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView ivEventImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View v;

        v = inflater.inflate(R.layout.grid_event_image, null);
        holder.ivEventImage = (ImageView) v.findViewById(R.id.ivEventImage);
        Picasso.with(mContext).load(mDataset.get(position)).into(holder.ivEventImage);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EventImageViewActivity.class);
                i.putExtra("URLs", mDataset);
                i.putExtra("POSITION", position);
                v.getContext().startActivity(i);
            }
        });

        return v;
    }

}