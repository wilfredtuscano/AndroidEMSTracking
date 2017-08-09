package com.example.wtuscano.emstracking.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.activity.DriverMainActivity;
import com.example.wtuscano.emstracking.activity.DriverMapsActivity;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.example.wtuscano.emstracking.jpa.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventDetailsFragment extends Fragment {

    private IDriverEventDetailsInteractionListener mListener;

    private TextView tvTitleValue;
    private TextView tvDateValue;
    private TextView tvCoordinatorValue;
    private TextView tvCoordinatorNumberValue;
    private TextView tvStartLocationValue;
    private TextView tvDestinationLocationValue;
    private Button btnRegisterForEvent;
    private Button btnNavToSource;
    private Button btnNavToDestination;
    private static SimpleDateFormat sdf;

    private Event currentEvent;
    private Bus currentBus;
    private ArrayList<Bus> busesForCurrrentEvent;
    private boolean containsCurrentBus = false;
    private Location currentLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference muidEvent;

    public EventDetailsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void intitializeComponents() {
        tvTitleValue = (TextView) getActivity().findViewById(R.id.tvTitleValue);
        tvDateValue = (TextView) getActivity().findViewById(R.id.tvDateValue);
        tvCoordinatorValue = (TextView) getActivity().findViewById(R.id.tvCoordinatorValue);
        tvCoordinatorNumberValue = (TextView) getActivity().findViewById(R.id.tvCoordinatorNumberValue);
        tvStartLocationValue = (TextView) getActivity().findViewById(R.id.tvStartLocationValue);
        tvDestinationLocationValue = (TextView) getActivity().findViewById(R.id.tvDestinationLocationValue);
        btnRegisterForEvent = (Button) getActivity().findViewById(R.id.btnRegisterForEvent);
        btnNavToSource = (Button) getActivity().findViewById(R.id.btnNavToSource);
        btnNavToDestination = (Button) getActivity().findViewById(R.id.btnNavToDestination);
        
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        sdf = new SimpleDateFormat("MM-dd-yyyy");

        currentEvent = mListener.getCurrentEvent();
        currentBus = mListener.getCurrentBus();

        tvTitleValue.setText(currentEvent.getTitle());
        tvDateValue.setText(sdf.format(currentEvent.getDateTime()));
        tvCoordinatorValue.setText(currentEvent.getCoordinatorName());
        tvCoordinatorNumberValue.setText(currentEvent.getCoordinatorNumber());
        tvStartLocationValue.setText(currentEvent.getStartLocation().getName());
        tvDestinationLocationValue.setText(currentEvent.getDestinationLocation().getName());

        muidEvent = databaseReference.child("EVENT");

        //Check if the driver has already registered for this event and set button attributes
        busesForCurrrentEvent = currentEvent.getBuses();
        containsCurrentBus = false;
        for (Bus busForCurrrentEvent : busesForCurrrentEvent) {
            if (busForCurrrentEvent.getNumber().equalsIgnoreCase(currentBus.getNumber())) {
                containsCurrentBus = true;
                break;
            }
        }
        setButtonProperties();

        btnRegisterForEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (containsCurrentBus) {
                    for (Bus busForCurrrentEvent2 : busesForCurrrentEvent) {
                        if (busForCurrrentEvent2.getNumber().equalsIgnoreCase(currentBus.getNumber())) {
                            busesForCurrrentEvent.remove(busForCurrrentEvent2);
                            break;
                        }
                    }
                    containsCurrentBus = false;
                } else {
                    busesForCurrrentEvent.add(currentBus);
                    containsCurrentBus = true;
                }
                currentEvent.setBuses(busesForCurrrentEvent);
                mListener.setCurrentEvent(currentEvent);

                //Save this to Firebase
                muidEvent.child(currentEvent.getKey()).setValue(currentEvent);
                setButtonProperties();
            }
        });

        btnNavToSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(v.getContext(), DriverMapsActivity.class);
                i.putExtra("CURRENT_EVENT", currentEvent);
                i.putExtra("CURRENT_BUS", currentBus);
                i.putExtra("NAV_TYPE", "TO_START");
                v.getContext().startActivity(i);*/

                currentLocation = getLastKnownLocation();
                ArrayList<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                latLngs.add(new LatLng(currentEvent.getStartLocation().getLatitude(), currentEvent.getStartLocation().getLongitude()));
                navigateUsingLatLngs(latLngs);
            }
        });

        btnNavToDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(currentEvent.getStartLocation().getLatitude(), currentEvent.getStartLocation().getLongitude()));
                latLngs.add(new LatLng(currentEvent.getDestinationLocation().getLatitude(), currentEvent.getDestinationLocation().getLongitude()));
                navigateUsingLatLngs(latLngs);
            }
        });
    }

    private void navigateUsingLatLngs(ArrayList<LatLng> latLngs) {
        String uri = "";
        for (LatLng latLng : latLngs) {
            if (TextUtils.isEmpty(uri)) {
                uri = String.format(
                        "http://maps.google.com/maps?saddr=%s, %s",
                        String.valueOf(latLng.latitude).replace(",", "."),
                        String.valueOf(latLng.longitude).replace(",", ".")
                );
            } else {
                if (!uri.contains("&daddr")) {
                    uri += String.format(
                            "&daddr=%s, %s",
                            String.valueOf(latLng.latitude).replace(",", "."),
                            String.valueOf(latLng.longitude).replace(",", ".")
                    );
                } else {
                    uri += String.format(
                            "+to:%s, %s",
                            String.valueOf(latLng.latitude).replace(",", "."),
                            String.valueOf(latLng.longitude).replace(",", ".")
                    );
                }
            }
        }

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(Intent.createChooser(intent, "Open With"));

        //startActivity(intent);
    }

    private void setButtonProperties() {
        if (containsCurrentBus) {
            btnRegisterForEvent.setText("CANCEL");
            btnNavToSource.setVisibility(View.VISIBLE);
            btnNavToDestination.setVisibility(View.VISIBLE);
        } else {
            btnRegisterForEvent.setText("REGISTER");
            btnNavToSource.setVisibility(View.GONE);
            btnNavToDestination.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        intitializeComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("GPS not enabled")
                    .setMessage("Would you like to enable GPS?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    getActivity().finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Update current location of driver for realtime tracking
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDriverEventDetailsInteractionListener) {
            mListener = (IDriverEventDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IDriverEventDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IDriverEventDetailsInteractionListener {
        Event getCurrentEvent();

        void setCurrentEvent(Event currentEvent);

        Bus getCurrentBus();
    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
