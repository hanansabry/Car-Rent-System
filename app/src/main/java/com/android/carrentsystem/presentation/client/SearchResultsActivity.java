package com.android.carrentsystem.presentation.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.CarsAdapter;
import com.android.carrentsystem.presentation.viewmodels.SearchCarsViewModel;
import com.android.carrentsystem.utils.Constants;

import javax.inject.Inject;

public class SearchResultsActivity extends DaggerAppCompatActivity {

    @BindView(R.id.cars_available_recycler_view)
    RecyclerView carsRecyclerView;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private SearchCarsViewModel searchCarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String category = intent.getStringExtra(Constants.CATEGORY);
        String model = intent.getStringExtra(Constants.MODEL);
        String type = intent.getStringExtra(Constants.TYPE);
        String year = intent.getStringExtra(Constants.YEAR);
        String from = intent.getStringExtra(Constants.FROM);
        String to = intent.getStringExtra(Constants.TO);

        searchCarViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(SearchCarsViewModel.class);
        searchCarViewModel.retrieveSearchCarResults(category, type, model, year, from, to);
        searchCarViewModel.observeCarSearchResultLiveData().observe(this, carList -> {
            if (carList != null && !carList.isEmpty()) {
                CarsAdapter carsAdapter = new CarsAdapter(carList, car -> {
                    Intent intent1 = new Intent(SearchResultsActivity.this, RentCarOrderActivity.class);
                    intent1.putExtra(Constants.CAR, car);
                    startActivity(intent1);
                });
                carsRecyclerView.setAdapter(carsAdapter);
            } else {
                Toast.makeText(this, "No available Cars", Toast.LENGTH_SHORT).show();
            }
        });
    }
}