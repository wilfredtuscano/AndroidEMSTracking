package com.example.wtuscano.emstracking.activity;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private EditText etSignupUsername;
    private EditText etSignupPassword;
    private EditText etConfirmPassword;
    private Button btnLogin;
    private Button btnSignup;
    private ProgressDialog pd;

    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        setTitle("EMS For Quicken loans");

        intitializeComponents();
    }

    private void intitializeComponents() {
        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        etSignupUsername = (EditText) findViewById(R.id.etSignupUsername);
        etSignupPassword = (EditText) findViewById(R.id.etSignupPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        pd = new ProgressDialog(AdminLoginActivity.this);
        pd.setCancelable(false);

        fAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(AdminLoginActivity.this, EventsTabbedActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login here
                if (validateLogin()) {
                    fAuth.signInWithEmailAndPassword(etLoginUsername.getText().toString() + "@QuickenLoans.com", etLoginPassword.getText().toString())
                            .addOnCompleteListener(AdminLoginActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(AdminLoginActivity.this, EventsTabbedActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(AdminLoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signup and Login here
                if (validateSignup()) {
                    pd.show();
                    fAuth.createUserWithEmailAndPassword(etSignupUsername.getText().toString() + "@QuickenLoans.com", etSignupPassword.getText().toString())
                            .addOnCompleteListener(AdminLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        Toast.makeText(AdminLoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(AdminLoginActivity.this, EventsTabbedActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(AdminLoginActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }

                            });
                }
            }
        });
    }

    private boolean validateLogin() {
        boolean valid = true;
        if (TextUtils.isEmpty(etLoginUsername.getText().toString())) {
            valid = false;
            etLoginUsername.setError("Required");
        }
        if (TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            valid = false;
            etLoginPassword.setError("Required");
        }

        return valid;
    }

    private boolean validateSignup() {
        boolean valid = true;
        if (TextUtils.isEmpty(etSignupUsername.getText().toString())) {
            valid = false;
            etSignupUsername.setError("Required");
        }
        if (TextUtils.isEmpty(etSignupPassword.getText().toString())) {
            valid = false;
            etSignupPassword.setError("Required");
        }
        if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
            valid = false;
            etConfirmPassword.setError("Required");
        }
        if ((!TextUtils.isEmpty(etSignupPassword.getText().toString())) && (!TextUtils.isEmpty(etConfirmPassword.getText().toString()))
                && (!etSignupPassword.getText().toString().equals(etConfirmPassword.getText().toString()))) {
            valid = false;
            etConfirmPassword.setError("Passwords dont match.");
        }

        return valid;
    }

    @Override
    protected void onStart() {
        fAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mAuthListener != null) {
            fAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }
}
