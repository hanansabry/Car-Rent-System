package com.android.carrentsystem.presentation.agency;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Violation;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.adapters.AgencyCarsAdapter;
import com.android.carrentsystem.presentation.viewmodels.AgencyCarsViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class CarListActivity extends DaggerAppCompatActivity {

    @BindView(R.id.agency_cars_recycler_view)
    RecyclerView carsRecyclerView;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private AgencyCarsViewModel agencyCarsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        ButterKnife.bind(this);

        agencyCarsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AgencyCarsViewModel.class);
        agencyCarsViewModel.retrieveAgencyCars(sharedPreferencesDataSource.getAgencyId());
        agencyCarsViewModel.observeCarListLiveData().observe(this, carList -> {
            if (carList != null && !carList.isEmpty()) {
                AgencyCarsAdapter carsAdapter = new AgencyCarsAdapter(carList, car -> {
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
                Toast.makeText(this, "No available Orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}