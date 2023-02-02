package com.android.carrentsystem.presentation.start;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.android.carrentsystem.R;
import com.android.carrentsystem.presentation.client.SearchCarsActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.get_started_button)
    public void onGetStartedClicked() {
        startActivity(new Intent(this, SearchCarsActivity.class));
    }

    @OnClick(R.id.agency_button)
    public void onLoginAsAgencyClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

}