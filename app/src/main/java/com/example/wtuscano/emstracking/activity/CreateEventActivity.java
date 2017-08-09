package com.example.wtuscano.emstracking.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.jpa.Event;
import com.example.wtuscano.emstracking.jpa.Location;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {

    private TextView tvCreateNewEvent;
    private EditText etEventTitle;
    private EditText etCoordinator;
    private EditText etCoordinatorNumber;
    private static TextView tvDatePicker;
    private Button btnCreateEvent;
    private PlaceAutocompleteFragment startLocationPicker;
    private PlaceAutocompleteFragment destinationLocationPicker;
    private ProgressDialog pd;
    private static SimpleDateFormat sdf;
    private static SimpleDateFormat sdf2;

    private static Date eventDate;
    private Location startLocation;
    private Location destinationLocation;
    private Event currentEvent;
    private boolean startLocationChanged = false;
    private boolean destinationLocationChanged = false;

    private FirebaseUser user;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference muidEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        setTitle("Event Details");

        intitializeComponents();
    }

    private void intitializeComponents() {
        tvCreateNewEvent = (TextView) findViewById(R.id.tvCreateNewEvent);
        etEventTitle = (EditText) findViewById(R.id.etEventTitle);
        etCoordinator = (EditText) findViewById(R.id.etCoordinator);
        etCoordinatorNumber = (EditText) findViewById(R.id.etCoordinatorNumber);
        tvDatePicker = (TextView) findViewById(R.id.tvDatePicker);
        btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);

        startLocation = new Location();
        startLocationPicker = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.startLocationPicker);

        destinationLocation = new Location();
        destinationLocationPicker = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destinationtLocationPicker);

        pd = new ProgressDialog(CreateEventActivity.this);
        pd.setCancelable(false);

        sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf2 = new SimpleDateFormat("YYYY-MM-DD");
        eventDate = new Date();
        tvDatePicker.setText(sdf.format(eventDate));
        muidEvent = databaseReference.child("EVENT");
        user = FirebaseAuth.getInstance().getCurrentUser();

        currentEvent = (Event) getIntent().getSerializableExtra("CURRENT_EVENT");
        if (currentEvent != null) {
            setAttritbutesOnScreen();
        }

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCreate()) {
                    pd.show();
                    Event event = new Event();
                    event.setTitle(etEventTitle.getText().toString());
                    event.setCoordinatorName(etCoordinator.getText().toString());
                    event.setCoordinatorNumber(etCoordinatorNumber.getText().toString());
                    event.setDateTime(eventDate);
                    event.setDateTimeToOrder(sdf2.format(eventDate));
                    event.setCreator(user.getEmail());
                    if (currentEvent != null) {
                        event.setKey(currentEvent.getKey());
                        if (startLocationChanged) {
                            event.setStartLocation(startLocation);
                        } else {
                            event.setStartLocation(currentEvent.getStartLocation());
                        }
                        if (destinationLocationChanged) {
                            event.setDestinationLocation(destinationLocation);
                        } else {
                            event.setDestinationLocation(currentEvent.getDestinationLocation());
                        }
                    } else {
                        event.setStartLocation(startLocation);
                        event.setDestinationLocation(destinationLocation);
                        event.setKey(muidEvent.push().getKey());
                    }

                    muidEvent.child(event.getKey()).setValue(event);
                    pd.dismiss();
                    finish();
                }
            }
        });

        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        startLocationPicker.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startLocationChanged = true;
                startLocation.setName(place.getName().toString());
                startLocation.setLatitude(place.getLatLng().latitude);
                startLocation.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("DEMO", "An error occurred: " + status);
            }
        });

        destinationLocationPicker.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destinationLocationChanged = true;
                destinationLocation.setName(place.getName().toString());
                destinationLocation.setLatitude(place.getLatLng().latitude);
                destinationLocation.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("DEMO", "An error occurred: " + status);
            }
        });
    }

    private void setAttritbutesOnScreen() {
        tvCreateNewEvent.setText("Edit Event Details");
        etEventTitle.setText(currentEvent.getTitle());
        etCoordinator.setText(currentEvent.getCoordinatorName());
        etCoordinatorNumber.setText(currentEvent.getCoordinatorNumber());
        tvDatePicker.setText(sdf.format(currentEvent.getDateTime()));
        startLocationPicker.setText(currentEvent.getStartLocation().getName());
        destinationLocationPicker.setText(currentEvent.getDestinationLocation().getName());
        btnCreateEvent.setText("SAVE CHANGES");
    }

    private boolean validateCreate() {
        boolean valid = true;
        if (TextUtils.isEmpty(etEventTitle.getText().toString())) {
            valid = false;
            etEventTitle.setError("Required");
        }
        if (TextUtils.isEmpty(etCoordinator.getText().toString())) {
            valid = false;
            etCoordinator.setError("Required");
        }
        if (TextUtils.isEmpty(etCoordinatorNumber.getText().toString())) {
            valid = false;
            etCoordinatorNumber.setError("Required");
        }
        if (!startLocationChanged) {
            if (currentEvent == null) {
                Toast.makeText(this, "No start location selected.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        if (!destinationLocationChanged) {
            if (currentEvent == null) {
                Toast.makeText(this, "No destination location selected.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        if (etCoordinatorNumber.getText().toString().trim().length() < 10) {
            Toast.makeText(this, "Co-ordinator number invalid.", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Date date = calendar.getTime();
            eventDate = (date);
            tvDatePicker.setText(sdf.format(eventDate));
        }
    }
}
