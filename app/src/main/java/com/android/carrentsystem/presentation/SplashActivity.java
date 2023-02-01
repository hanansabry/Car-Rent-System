package com.android.carrentsystem.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.carrentsystem.R;
import com.android.carrentsystem.presentation.agency.AgencyDashboardActivity;
import com.android.carrentsystem.presentation.start.StartActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, AgencyDashboardActivity.class));
            }
        }, SPLASH_TIME_OUT);
    }
}