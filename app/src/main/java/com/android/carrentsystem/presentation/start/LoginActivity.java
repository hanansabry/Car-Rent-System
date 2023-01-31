package com.android.carrentsystem.presentation.start;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.presentation.agency.AgencyDashboardActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_button)
    public void onLoginClicked() {
        Toast.makeText(this, "logined", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AgencyDashboardActivity.class));
    }

    @OnClick(R.id.register_text_view)
    public void onRegisterClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}