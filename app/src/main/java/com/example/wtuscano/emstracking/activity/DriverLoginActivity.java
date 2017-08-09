package com.example.wtuscano.emstracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wtuscano.emstracking.R;
import com.example.wtuscano.emstracking.jpa.Bus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLoginActivity extends AppCompatActivity {

    private static final String DRIVER_USERNAME = "EMSAppDriver@quickenloans.com";
    private static final String DRIVER_PASSWORD = "EMSAppPassword";

    private EditText etFname;
    private EditText etLname;
    private EditText etBusNumber;
    private EditText etDriverContact;
    private Button btnLogin;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        setTitle("EMS For Quicken loans");

        intitializeComponents();
    }

    private void intitializeComponents() {
        etFname = (EditText) findViewById(R.id.etFname);
        etLname = (EditText) findViewById(R.id.etLname);
        etBusNumber = (EditText) findViewById(R.id.etBusNumber);
        etDriverContact = (EditText) findViewById(R.id.etDriverContact);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        fAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login here
                if (validateLogin()) {
                    final Bus bus = new Bus();
                    bus.setDriverFname(etFname.getText().toString());
                    bus.setDriverLname(etLname.getText().toString());
                    bus.setNumber(etBusNumber.getText().toString());
                    bus.setDriverContactNumber(etDriverContact.getText().toString());

                    fAuth.signInWithEmailAndPassword(DRIVER_USERNAME, DRIVER_PASSWORD)
                            .addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DriverLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(DriverLoginActivity.this, EventsActivity.class);
                                        i.putExtra("CURRENT_BUS", bus);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(DriverLoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean validateLogin() {
        boolean valid = true;
        if (TextUtils.isEmpty(etFname.getText().toString())) {
            valid = false;
            etFname.setError("Required");
        }
        if (TextUtils.isEmpty(etLname.getText().toString())) {
            valid = false;
            etLname.setError("Required");
        }
        if (TextUtils.isEmpty(etBusNumber.getText().toString())) {
            valid = false;
            etBusNumber.setError("Required");
        }
        if (TextUtils.isEmpty(etDriverContact.getText().toString())) {
            valid = false;
            etDriverContact.setError("Required");
        }
        if (etDriverContact.getText().toString().trim().length() < 10) {
            Toast.makeText(this, "Contact number invalid.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
