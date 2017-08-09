package com.example.wtuscano.emstracking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.activity.EventsTabbedActivity;
import com.example.wtuscano.emstracking.adapter.RecyclerMyEventsAdapter;
import com.example.wtuscano.emstracking.jpa.Event;

import java.util.ArrayList;

public class EventsListFragment extends Fragment {

    RecyclerView rvMyEvents;
    RecyclerMyEventsAdapter rvMyEventsAdapter;

    public EventsListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_tabbed, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        /*RecyclerView rvMyEvents;
        RecyclerMyEventsAdapter rvMyEventsAdapter;*/

        rvMyEvents = (RecyclerView) rootView.findViewById(R.id.rvEventsList);
        rvMyEventsAdapter = new RecyclerMyEventsAdapter((ArrayList<Event>) getArguments().getSerializable(EventsTabbedActivity.ARG_EVENTS));
        rvMyEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMyEvents.setAdapter(rvMyEventsAdapter);

        textView.setText(getString(R.string.section_format, getArguments().getInt(EventsTabbedActivity.ARG_SECTION_NUMBER)));
        return rootView;
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rvMyEvents != null) {
            rvMyEventsAdapter = new RecyclerMyEventsAdapter((ArrayList<Event>) getArguments().getSerializable(EventsTabbedActivity.ARG_EVENTS));
            rvMyEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMyEvents.setAdapter(rvMyEventsAdapter);
        }
    }*/


}
