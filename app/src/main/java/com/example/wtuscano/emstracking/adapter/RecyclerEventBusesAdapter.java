package com.example.wtuscano.emstracking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.activity.AdminMainActivity;
import com.example.wtuscano.emstracking.activity.DriverMainActivity;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RecyclerEventBusesAdapter extends RecyclerView.Adapter<RecyclerEventBusesAdapter.ViewHolder> {

    public static ArrayList<Bus> mDataset;
    public Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCv;
        TextView tvBusNumber;
        TextView tvFName;
        TextView tvLName;
        //TextView tvCurrentLocation;

        public ViewHolder(View v) {
            super(v);
            mCv = (CardView) v.findViewById(R.id.cvEventBusDetails);
            tvBusNumber = (TextView) v.findViewById(R.id.tvBusNumber);
            tvFName = (TextView) v.findViewById(R.id.tvFName);
            tvLName = (TextView) v.findViewById(R.id.tvLName);
            //tvCurrentLocation = (TextView) v.findViewById(R.id.tvCurrentLocation);
        }
    }

    public RecyclerEventBusesAdapter(ArrayList<Bus> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_event_buses_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        mContext = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvBusNumber.setText(mDataset.get(position).getNumber());
        holder.tvFName.setText(mDataset.get(position).getDriverFname());
        holder.tvLName.setText(mDataset.get(position).getDriverLname());
        //holder.tvCurrentLocation.setText("Show drivers location here.");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
