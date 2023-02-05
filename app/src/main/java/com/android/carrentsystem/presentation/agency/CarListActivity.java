package com.android.carrentsystem.presentation.agency;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.Violation;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.adapters.AgencyCarsAdapter;
import com.android.carrentsystem.presentation.viewmodels.AgencyCarsViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class CarListActivity extends DaggerAppCompatActivity implements ViolationListBottomSheetDialog.CarViolationsCallback {

    @BindView(R.id.agency_cars_recycler_view)
    RecyclerView carsRecyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private AgencyCarsViewModel agencyCarsViewModel;
    private Car selectedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        ButterKnife.bind(this);

        agencyCarsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AgencyCarsViewModel.class);
        agencyCarsViewModel.retrieveAgencyCars(sharedPreferencesDataSource.getAgencyId());
        agencyCarsViewModel.observeCarListLiveData().observe(this, carList -> {
            progressBar.setVisibility(View.GONE);
            if (carList != null && !carList.isEmpty()) {
                carsRecyclerView.setVisibility(View.VISIBLE);
                AgencyCarsAdapter carsAdapter = new AgencyCarsAdapter(carList, car -> {
                    selectedCar = car;
                    if (car.getViolationList() != null && !car.getViolationList().isEmpty()) {
                        ArrayList<Violation> violationList = new ArrayList<>();
                        for (String id : car.getViolationList().keySet()) {
                            violationList.add(car.getViolationList().get(id));
                        }
                        ViolationListBottomSheetDialog violationsDialog = ViolationListBottomSheetDialog.newInstance(violationList);
                        violationsDialog.show(getSupportFragmentManager(), ViolationListBottomSheetDialog.TAG);
                    } else {
                        Toast.makeText(this, "No current violations", Toast.LENGTH_SHORT).show();
                    }
                });
                carsRecyclerView.setAdapter(carsAdapter);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        });

        agencyCarsViewModel.observeErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onViolationsDone() {
        agencyCarsViewModel.setViolationsDone(selectedCar.getId());
        agencyCarsViewModel.observeViolationsDoneState().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                agencyCarsViewModel.retrieveAgencyCars(sharedPreferencesDataSource.getAgencyId());
            } else {
                Toast.makeText(this, "Can't complete process, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}