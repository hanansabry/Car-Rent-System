package com.android.carrentsystem.presentation.start;

import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.agency.AgencyDashboardActivity;
import com.android.carrentsystem.presentation.viewmodels.AuthenticationViewModel;

import javax.inject.Inject;

public class LoginActivity extends DaggerAppCompatActivity {

    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.login_button)
    Button signIn;
    @BindView(R.id.login_progress_bar)
    ProgressBar loginProgressBar;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        authenticationViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AuthenticationViewModel.class);
        authenticationViewModel.observeUserState().observe(this, agency -> {
            if (agency != null) {
                signIn.setVisibility(View.VISIBLE);
                loginProgressBar.setVisibility(View.GONE);
                sharedPreferencesDataSource.setAgencyId(agency.getId());
                sharedPreferencesDataSource.setAgencyName(agency.getName());
                startActivity(new Intent(this, AgencyDashboardActivity.class));
                finish();
            }
        });
        authenticationViewModel.observeErrorState().observe(this, error -> {
            if (error != null) {
                if (error.contains("badly formatted")) {
                    Toast.makeText(LoginActivity.this, R.string.email_address_format_err_msg, Toast.LENGTH_SHORT).show();
                } else if (error.contains("no user record")) {
                    Toast.makeText(LoginActivity.this, R.string.user_not_found_err_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.sign_in_general_error_msg + "\n" + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.login_button)
    public void onLoginClicked() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {
            authenticationViewModel.loginAsAgency(email, password);
        } else {
            Toast.makeText(this, R.string.email_password_required_2, Toast.LENGTH_SHORT).show();
        }
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