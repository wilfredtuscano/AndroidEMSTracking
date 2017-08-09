package com.example.wtuscano.emstracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.wtuscano.emstracking.R;

public class MainActivity extends AppCompatActivity {

    Button btnLoginAsAdmin;
    Button btnLoginAsDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("EMS For Quicken loans");
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(ic_launcher);*/

        intitializeComponents();
    }

    private void intitializeComponents() {
        btnLoginAsAdmin = (Button) findViewById(R.id.btnLoginAsAdmin);
        btnLoginAsDriver = (Button) findViewById(R.id.btnLoginAsDriver);

        btnLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Admin login intent here
                Intent i = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLoginAsDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Driver login intent here
                Intent i = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
