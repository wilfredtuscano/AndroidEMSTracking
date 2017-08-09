package com.example.wtuscano.emstracking.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.adapter.RecyclerMyEventsAdapter;
import com.example.wtuscano.emstracking.fragment.AdminBusesFragment;
import com.example.wtuscano.emstracking.fragment.AdminDocsFragment;
import com.example.wtuscano.emstracking.fragment.AdminMapFragment;
import com.example.wtuscano.emstracking.fragment.EventsListFragment;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class EventsTabbedActivity extends AppCompatActivity implements RecyclerMyEventsAdapter.IInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_EVENTS = "events";
    private ArrayList<Event> allEvents = new ArrayList<>();
    private ArrayList<Event> upcomingEvents = new ArrayList<>();
    private ArrayList<Event> completedEvents = new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference muidEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_tabbed);

        intitializeComponents();
    }

    private void intitializeComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        muidEvent = databaseReference.child("EVENT");
        muidEvent.orderByChild("dateTimeToOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allEvents.clear();
                upcomingEvents.clear();
                completedEvents.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Event event = d.getValue(Event.class);
                    allEvents.add(event);
                    if ((new Date()).before(event.getDateTime())) {
                        upcomingEvents.add(event);
                    } else {
                        completedEvents.add(event);
                    }
                }
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                //mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    EventsListFragment upcomingFragment = new EventsListFragment();
                    Bundle upcomingArgs = new Bundle();
                    upcomingArgs.putInt(ARG_SECTION_NUMBER, position + 1);
                    upcomingArgs.putSerializable(ARG_EVENTS, upcomingEvents);
                    upcomingFragment.setArguments(upcomingArgs);
                    return upcomingFragment;
                case 1:
                    EventsListFragment completedFragment = new EventsListFragment();
                    Bundle completedArgs = new Bundle();
                    completedArgs.putInt(ARG_SECTION_NUMBER, position + 1);
                    completedArgs.putSerializable(ARG_EVENTS, completedEvents);
                    completedFragment.setArguments(completedArgs);
                    return completedFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "UPCOMING";
                case 1:
                    return "COMPLETED";
            }
            return null;
        }
    }

    @Override
    public Bus getCurrentBus() {
        return null;
    }
}
