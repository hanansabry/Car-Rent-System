package com.android.carrentsystem.presentation.client;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.CarModel;
import com.android.carrentsystem.data.models.CarType;
import com.android.carrentsystem.data.models.RentOrder;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.agency.OrderDetailsActivity;
import com.android.carrentsystem.presentation.viewmodels.SearchCarsViewModel;
import com.android.carrentsystem.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class SearchCarsActivity extends DaggerAppCompatActivity {

    @BindView(R.id.select_category_spinner)
    Spinner categoriesSpinner;
    @BindView(R.id.type_spinner)
    Spinner typesSpinner;
    @BindView(R.id.model_spinner)
    Spinner modelsSpinner;
    @BindView(R.id.year_spinner)
    Spinner yearsSpinner;
    @BindView(R.id.from_date_edit_text)
    EditText fromDateEditText;
    @BindView(R.id.to_date_edit_text)
    EditText toDateEditText;
    @BindView(R.id.search_orders_by_phone_edit_text)
    EditText searchByPhoneEditText;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private SearchCarsViewModel searchCarViewModel;
    private CarCategory selectedCategory;
    private CarType selectedType;
    private CarModel selectedModel;
    private String selectedYear;
    private long fromDay;
    private long toDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);
        ButterKnife.bind(this);

        searchCarViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(SearchCarsViewModel.class);
        searchCarViewModel.retrieveCarCategories();
        searchCarViewModel.observeErrorState().observe(this, error -> {
            if (error.equals(Constants.NO_AVAILABLE_ORDERS)) {
                Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
        initiateSpinners();
    }

    private void initiateSpinners() {
        searchCarViewModel.observeRetrieveCarCategoriesLiveData().observe(this, this::setCategoriesSpinner);
    }

    private void setCategoriesSpinner(List<CarCategory> carCategories) {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Category");
        for (CarCategory carCategory : carCategories) {
            categoryNames.add(carCategory.getName());
        }
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        categoriesAdapter.addAll(categoryNames);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedCategory = carCategories.get(position - 1);
                    setTypesSpinner(selectedCategory.getTypes());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTypesSpinner(List<CarType> types) {
        List<String> typesNames = new ArrayList<>();
        typesNames.add("Type");
        for (CarType carType : types) {
            typesNames.add(carType.getName());
        }
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        typesAdapter.addAll(typesNames);
        typesSpinner.setAdapter(typesAdapter);
        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedType = types.get(position - 1);
                    setModelsSpinner(selectedType.getModels());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setModelsSpinner(List<CarModel> models) {
        List<String> modelsNames = new ArrayList<>();
        modelsNames.add("Model");
        for (CarModel carModel : models) {
            modelsNames.add(carModel.getName());
        }
        ArrayAdapter<String> modelsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        modelsAdapter.addAll(modelsNames);
        modelsSpinner.setAdapter(modelsAdapter);
        modelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedModel = models.get(position - 1);
                    setYearsSpinner(selectedModel.getYears());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setYearsSpinner(List<String> years) {
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        yearsAdapter.add("Year");
        yearsAdapter.addAll(years);
        yearsSpinner.setAdapter(yearsAdapter);
        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedYear = years.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener dateSetListener) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.from_date_layout)
    public void onFromDatePicked() {
        showDatePicker((view, year, monthOfYear, dayOfMonth) -> {
            fromDateEditText.setText(String.format(Locale.getDefault(),
                    "%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);
            fromDay = selectedDate.getTimeInMillis();
        });
    }

    @OnClick(R.id.to_date_layout)
    public void onToDatePicked() {
        showDatePicker((view, year, monthOfYear, dayOfMonth) -> {
            toDateEditText.setText(String.format(Locale.getDefault(),
                    "%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);
            toDay = selectedDate.getTimeInMillis();
        });
    }

    @OnClick(R.id.search_cars_button)
    public void onSearchCarsClicked() {
        if (selectedCategory == null
                || selectedType == null
                || selectedModel == null
                || selectedYear == null
                || fromDateEditText.getText().toString().isEmpty()
                || toDateEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.all_fields_required_message, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(Constants.CATEGORY, selectedCategory.getName());
            intent.putExtra(Constants.MODEL, selectedModel.getName());
            intent.putExtra(Constants.TYPE, selectedType.getName());
            intent.putExtra(Constants.YEAR, selectedYear);
            intent.putExtra(Constants.FROM, fromDay);
            intent.putExtra(Constants.TO, toDay);
            long daysNum = TimeUnit.DAYS.convert((toDay - fromDay), TimeUnit.MILLISECONDS);
            intent.putExtra(Constants.NUM_DAYS, daysNum);
            startActivity(intent);
        }
    }

    @OnClick(R.id.search_orders_button)
    public void onSearchOrdersClicked() {
        String phoneNumber = searchByPhoneEditText.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "You must enter phone number", Toast.LENGTH_SHORT).show();
        } else {
            searchCarViewModel.retrieveOrdersByPhone(phoneNumber);
            Observer<RentOrder> searchOrdersObserver = new Observer<RentOrder>() {
                @Override
                public void onChanged(RentOrder order) {
                    if (order != null) {
                        Intent intent = new Intent(SearchCarsActivity.this, OrderDetailsActivity.class);
                        intent.putExtra(Constants.ORDER, order);
                        intent.putExtra(Constants.IS_CLIENT, true);
                        startActivity(intent);
                        searchCarViewModel.observeOrderLiveDate().removeObserver(this);
                    }
                }
            };
            searchCarViewModel.observeOrderLiveDate().observe(this, searchOrdersObserver);
        }
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}