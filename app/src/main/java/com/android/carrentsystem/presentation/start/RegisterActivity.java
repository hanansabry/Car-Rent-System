package com.android.carrentsystem.presentation.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Agency;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.agency.AgencyDashboardActivity;
import com.android.carrentsystem.presentation.viewmodels.AuthenticationViewModel;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class RegisterActivity extends DaggerAppCompatActivity {

    @BindView(R.id.agency_name_edit_text)
    EditText agecnyNameEditText;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.phone_edit_text)
    EditText phoneEditText;
    @BindView(R.id.address_edit_text)
    EditText addressEditText;
    @BindView(R.id.register_button)
    Button signUpButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        authenticationViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AuthenticationViewModel.class);
        observeSignUp();
        observeError();
    }

    private void observeSignUp() {
        authenticationViewModel.observeUserState().observe(this, agency -> {
            if (agency != null) {
                progressBar.setVisibility(View.GONE);
                signUpButton.setVisibility(View.VISIBLE);
                sharedPreferencesDataSource.setAgencyId(agency.getId());
                sharedPreferencesDataSource.setAgencyName(agency.getName());
                Intent intent = new Intent(RegisterActivity.this, AgencyDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void observeError() {
        authenticationViewModel.observeErrorState().observe(this, error -> {
            if (error != null) {
                if (error.contains("password is invalid")) {
                    Toast.makeText(RegisterActivity.this, R.string.password_invalid_err_msg, Toast.LENGTH_SHORT).show();
                } else if (error.contains("badly formatted")) {
                    Toast.makeText(RegisterActivity.this, R.string.email_address_format_err_msg, Toast.LENGTH_SHORT).show();
                } else if (error.contains("already in use")) {
                    Toast.makeText(RegisterActivity.this, R.string.already_in_use_err_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.sign_up_general_error_msg + "\n" + error, Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.GONE);
            signUpButton.setVisibility(View.VISIBLE);
        });
    }

    @OnClick(R.id.register_button)
    public void onRegisterClicked() {
        String agencyName = agecnyNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        if (email.isEmpty()
                || password.isEmpty()
                || agencyName.isEmpty()
                || phone.isEmpty()
                || address.isEmpty()
        ) {
            Toast.makeText(this, R.string.all_fields_required_message, Toast.LENGTH_SHORT).show();
        } else {
            Agency agency = new Agency(
                    agencyName, email, password, address, phone
            );
            authenticationViewModel.registerNewAgency(agency);
            progressBar.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.GONE);
        }
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