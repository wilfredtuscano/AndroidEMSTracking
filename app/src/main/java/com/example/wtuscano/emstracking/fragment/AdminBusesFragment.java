package com.example.wtuscano.emstracking.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.activity.EventsActivity;
import com.example.wtuscano.emstracking.adapter.RecyclerEventBusesAdapter;
import com.example.wtuscano.emstracking.adapter.RecyclerMyEventsAdapter;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;

public class AdminBusesFragment extends Fragment {

    private IAdminBusesInteractionListener mListener;

    private RecyclerView rvEventBuses;
    private RecyclerEventBusesAdapter rvEventBusesAdapter;

    private Event currentEvent;

    public AdminBusesFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intitializeComponents();
    }

    private void intitializeComponents() {
        currentEvent = mListener.getCurrentEvent();

        rvEventBuses = (RecyclerView) getActivity().findViewById(R.id.rvEventBusesList);
        rvEventBusesAdapter = new RecyclerEventBusesAdapter(currentEvent.getBuses());
        rvEventBuses.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEventBuses.setAdapter(rvEventBusesAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_buses, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAdminBusesInteractionListener) {
            mListener = (IAdminBusesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IAdminBusesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IAdminBusesInteractionListener {
        Event getCurrentEvent();
    }
}
