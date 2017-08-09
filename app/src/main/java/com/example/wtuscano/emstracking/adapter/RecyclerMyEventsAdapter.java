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


public class RecyclerMyEventsAdapter extends RecyclerView.Adapter<RecyclerMyEventsAdapter.ViewHolder> {

    //Created this Interface to get the CurrentBus object because we are reusing the Events Intent for Admin and Driver both
    public static IInteractionListener mListener;

    public static ArrayList<Event> mDataset;
    public Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCv;
        TextView tvEventName;
        TextView tvStartLocation;
        TextView tvDestinationLocation;
        TextView tvEventDate;
        SimpleDateFormat sdf;

        public ViewHolder(View v) {
            super(v);
            mCv = (CardView) v.findViewById(R.id.cvEventDetails);
            tvEventName = (TextView) v.findViewById(R.id.tvEventTitle);
            tvStartLocation = (TextView) v.findViewById(R.id.tvStartLocation);
            tvDestinationLocation = (TextView) v.findViewById(R.id.tvDestination);
            tvEventDate = (TextView) v.findViewById(R.id.tvEventDate);

            sdf = new SimpleDateFormat("MM-dd-yyyy");

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to Admin Main activity
                    if (mListener.getCurrentBus() != null) {
                        Intent i = new Intent(v.getContext(), DriverMainActivity.class);
                        i.putExtra("CURRENT_EVENT", mDataset.get(getAdapterPosition()));
                        i.putExtra("CURRENT_BUS", mListener.getCurrentBus());
                        v.getContext().startActivity(i);
                    } else {
                        Intent i = new Intent(v.getContext(), AdminMainActivity.class);
                        i.putExtra("CURRENT_EVENT", mDataset.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                    }
                }
            });
        }
    }

    public RecyclerMyEventsAdapter(ArrayList<Event> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_my_events_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        mContext = parent.getContext();
        mListener = (IInteractionListener) mContext;
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvEventName.setText(mDataset.get(position).getTitle());
        holder.tvStartLocation.setText(mDataset.get(position).getStartLocation().getName());
        holder.tvDestinationLocation.setText(mDataset.get(position).getDestinationLocation().getName());
        holder.tvEventDate.setText(holder.sdf.format(mDataset.get(position).getDateTime()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface IInteractionListener {
        Bus getCurrentBus();
    }

}
