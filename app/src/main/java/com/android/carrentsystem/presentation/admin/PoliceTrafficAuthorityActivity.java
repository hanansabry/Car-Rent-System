package com.android.carrentsystem.presentation.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.Violation;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.viewmodels.PoliceTrafficViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class PoliceTrafficAuthorityActivity extends DaggerAppCompatActivity {

    @BindView(R.id.plat_num_spinner)
    Spinner plateNumSpinner;
    @BindView(R.id.fees_edit_text)
    EditText feesEditText;
    @BindView(R.id.description_edit_text)
    EditText descEditText;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private PoliceTrafficViewModel policeTrafficViewModel;
    private Car selectedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_traffic_authority);
        ButterKnife.bind(this);

        policeTrafficViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(PoliceTrafficViewModel.class);
        policeTrafficViewModel.observeErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
        setPlateNumbersSpinner();
    }

    private void setPlateNumbersSpinner() {
        policeTrafficViewModel.retrieveAllCars();
        policeTrafficViewModel.observeCarListLiveData().observe(this, carList -> {
            if (carList != null && !carList.isEmpty()) {
                List<String> plateNumbers = new ArrayList<>();
                plateNumbers.add("Plate Number");
                for (Car car : carList) {
                    plateNumbers.add(car.getPlatNum());
                }
                ArrayAdapter<String> platesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
                platesAdapter.addAll(plateNumbers);
                plateNumSpinner.setAdapter(platesAdapter);
                plateNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            selectedCar = carList.get(position - 1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    @OnClick(R.id.submit_button)
    public void onSubmitClicked() {
        if (selectedCar == null
                || feesEditText.getText().toString().isEmpty()
                || descEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.all_fields_required_message, Toast.LENGTH_SHORT).show();
        } else {
            //get current date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date = simpleDateFormat.format(System.currentTimeMillis());
            Violation violation = new Violation();
            violation.setNotes(descEditText.getText().toString());
            violation.setCost(Double.parseDouble(feesEditText.getText().toString()));
            violation.setDate(date);

            policeTrafficViewModel.addNewViolation(selectedCar.getId(), violation);
            policeTrafficViewModel.observeViolationAddStatusLiveData().observe(this, success -> {
                if (success) {
                    Toast.makeText(this, "Violation is added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Can't add violation, Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}