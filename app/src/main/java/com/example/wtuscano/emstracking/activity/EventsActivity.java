package com.example.wtuscano.emstracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.adapter.RecyclerMyEventsAdapter;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class EventsActivity extends AppCompatActivity implements RecyclerMyEventsAdapter.IInteractionListener {

    private FloatingActionButton fabCreateEvent;
    private RecyclerView rvMyEvents;
    private RecyclerMyEventsAdapter rvMyEventsAdapter;

    private String callingClass;
    private ArrayList<Event> allEvents = new ArrayList<>();
    private ArrayList<Event> upcomingEvents = new ArrayList<>();
    private ArrayList<Event> completedEvents = new ArrayList<>();
    private Bus currentBus;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference muidEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        setTitle("Events List");

        intitializeComponents();
    }

    private void intitializeComponents() {
        currentBus = (Bus) getIntent().getSerializableExtra("CURRENT_BUS");
        callingClass = getIntent().getStringExtra("CALLING_CLASS");

        fabCreateEvent = (FloatingActionButton) findViewById(R.id.fabCreateEvent);
        if (currentBus != null || callingClass != null) {
            fabCreateEvent.hide();
        } else {
            fabCreateEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(EventsActivity.this, CreateEventActivity.class);
                    startActivity(i);
                }
            });
        }

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

                rvMyEvents = (RecyclerView) findViewById(R.id.rvEventsList);
                if (callingClass != null) {
                    rvMyEventsAdapter = new RecyclerMyEventsAdapter(completedEvents);
                } else {
                    rvMyEventsAdapter = new RecyclerMyEventsAdapter(upcomingEvents);
                }
                rvMyEvents.setLayoutManager(new LinearLayoutManager(EventsActivity.this));
                rvMyEvents.setAdapter(rvMyEventsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (callingClass == null && currentBus ==null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.events, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (callingClass == null && currentBus ==null) {
            switch (item.getItemId()) {
                case R.id.completedEvents:
                    Intent i = new Intent(EventsActivity.this, EventsActivity.class);
                    i.putExtra("CALLING_CLASS", "EventsActivity");
                    startActivity(i);
                    finish();
                    return true;

                case R.id.logOut:
                    FirebaseAuth.getInstance().signOut();
                    Intent iLogout = new Intent(EventsActivity.this, MainActivity.class);
                    startActivity(iLogout);
                    finish();
                    return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (currentBus == null) {
            if (callingClass == null) {
                finish();
            } else {
                Intent i = new Intent(EventsActivity.this, EventsActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            FirebaseAuth.getInstance().signOut();
            Intent iLogout = new Intent(EventsActivity.this, MainActivity.class);
            startActivity(iLogout);
            finish();
        }
    }

    @Override
    public Bus getCurrentBus() {
        return currentBus;
    }
}
