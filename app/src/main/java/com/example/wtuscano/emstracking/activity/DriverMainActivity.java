package com.example.wtuscano.emstracking.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.fragment.AdminDocsFragment;
import com.example.wtuscano.emstracking.fragment.EventDetailsFragment;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;

public class DriverMainActivity extends AppCompatActivity implements EventDetailsFragment.IDriverEventDetailsInteractionListener,
        AdminDocsFragment.IAdminDocsInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static final String CLASS_NAME = "DriverMainActivity";
    private Event currentEvent = new Event();
    private Bus currentBus = new Bus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        intitializeComponents();
    }

    private void intitializeComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        currentEvent = (Event) getIntent().getSerializableExtra("CURRENT_EVENT");
        currentBus = (Bus) getIntent().getSerializableExtra("CURRENT_BUS");

        setTitle(currentEvent.getTitle());
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // Open EventDetailsFragment
                case 0:
                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    return eventDetailsFragment;
                // Open AdminDocsFragment
                case 1:
                    AdminDocsFragment adminDocsFragment = new AdminDocsFragment();
                    return adminDocsFragment;
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
                    return "EVENT DETAILS";
                case 1:
                    return "DOCS";
            }
            return null;
        }
    }

    //IDriverEventDetailsInteractionListener and IAdminDocsInteractionListener methods
    @Override
    public Event getCurrentEvent() {
        return currentEvent;
    }

    @Override
    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    @Override
    public Bus getCurrentBus() {
        return currentBus;
    }

    //IAdminDocsInteractionListener methods
    @Override
    public String getParentClassName() {
        return DriverMainActivity.CLASS_NAME;
    }
}
