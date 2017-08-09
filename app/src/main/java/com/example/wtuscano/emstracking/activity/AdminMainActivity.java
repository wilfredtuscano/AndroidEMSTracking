package com.example.wtuscano.emstracking.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.fragment.AdminBusesFragment;
import com.example.wtuscano.emstracking.fragment.AdminDocsFragment;
import com.example.wtuscano.emstracking.fragment.AdminMapFragment;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;

public class AdminMainActivity extends AppCompatActivity implements AdminDocsFragment.IAdminDocsInteractionListener,
        AdminMapFragment.IAdminMapInteractionListener, AdminBusesFragment.IAdminBusesInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static final String CLASS_NAME = "AdminMainActivity";
    private Event currentEvent = new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

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

        setTitle(currentEvent.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editEvent:
                Intent i = new Intent(AdminMainActivity.this, CreateEventActivity.class);
                i.putExtra("CURRENT_EVENT", currentEvent);
                startActivity(i);
                return true;
        }
        return false;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // Open AdminDocsFragment
                case 0:
                    AdminDocsFragment adminDocsFragment = new AdminDocsFragment();
                    return adminDocsFragment;
                // Open AdminMapFragment
                case 1:
                    AdminMapFragment adminMapFragment = new AdminMapFragment();
                    return adminMapFragment;
                // Open AdminBusesFragment
                case 2:
                    AdminBusesFragment adminBusesFragment = new AdminBusesFragment();
                    return adminBusesFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DOCS";
                case 1:
                    return "MAP";
                case 2:
                    return "BUSES";
            }
            return null;
        }
    }

    //Interface methods
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
        return null;
    }

    @Override
    public String getParentClassName() {
        return AdminMainActivity.CLASS_NAME;
    }

    @Override
    public void onMapInteraction(Uri uri) {

    }
}
