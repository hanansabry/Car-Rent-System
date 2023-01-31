package com.android.carrentsystem.presentation.start;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.presentation.agency.AgencyDashboardActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void onRegisterClicked() {
        Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AgencyDashboardActivity.class));
    }

    @OnClick(R.id.login_text_view)
    public void onLoginClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}