package com.android.carrentsystem.presentation.agency;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.CarModel;
import com.android.carrentsystem.data.models.CarType;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.images.GalleryImagesSelector;
import com.android.carrentsystem.presentation.images.ImagesSelector;
import com.android.carrentsystem.presentation.viewmodels.AddCarViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class AddCarActivity extends DaggerAppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;

    @BindView(R.id.select_category_spinner)
    Spinner categoriesSpinner;
    @BindView(R.id.type_spinner)
    Spinner typesSpinner;
    @BindView(R.id.model_spinner)
    Spinner modelsSpinner;
    @BindView(R.id.year_spinner)
    Spinner yearsSpinner;
    @BindView(R.id.color_spinner)
    Spinner colorsSpinner;
    @BindView(R.id.plat_num_edit_text)
    EditText platNumEditText;
    @BindView(R.id.description_edit_text)
    EditText descEditText;
    @BindView(R.id.price_edit_text)
    EditText priceEditText;
    @BindView(R.id.upload_images_edit_text)
    EditText uploadImagesEditText;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private AddCarViewModel addCarViewModel;
    private CarCategory selectedCategory;
    private CarType selectedType;
    private CarModel selectedModel;
    private String selectedYear;
    private List<Uri> carImagesUris = new ArrayList<>();
    private List<String> uploadedCarImagesUris = new ArrayList<>();
    private GalleryImagesSelector imagesSelector;
    private ProgressDialog loadingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        ButterKnife.bind(this);
        loadingProgressDialog = new ProgressDialog(this);
        loadingProgressDialog.setMessage("Loading");
        imagesSelector = new GalleryImagesSelector(AddCarActivity.this);
        addCarViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AddCarViewModel.class);
        addCarViewModel.retrieveCarCategories();
        initiateSpinners();
    }

    private void initiateSpinners() {
        addCarViewModel.observeRetrieveCarCategoriesLiveData().observe(this, this::setCategoriesSpinner);
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

    @OnClick(R.id.submit_button)
    public void onSubmitClicked() {
        Car newCar = validateCarProperties();
        if (newCar != null) {
            loadingProgressDialog.show();
            // add new car to database
            //upload carImagesFirstly
            for (Uri imageUri : carImagesUris) {
                addCarViewModel
                        .uploadImageToFirebaseStorage(
                                imageUri,
                                sharedPreferencesDataSource.getAgencyId(),
                                platNumEditText.getText().toString()
                        );
            }
            addCarViewModel.observeUploadImageState().observe(this, url -> {
                uploadedCarImagesUris.add(url);
                if (carImagesUris.size() == uploadedCarImagesUris.size()) {
                    newCar.setCarImagesUrls(uploadedCarImagesUris);
                    addCarViewModel.addNewCar(sharedPreferencesDataSource.getAgencyId(), newCar);
                    addCarViewModel.observeUploadImageState().observe(this, success -> {
                        loadingProgressDialog.dismiss();
                        Toast.makeText(this, "Car is added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        } else {
            Toast.makeText(this, R.string.all_fields_required_message, Toast.LENGTH_SHORT).show();
        }
    }

    private Car validateCarProperties() {
        String selectedColor = colorsSpinner.getSelectedItemPosition() == 0 ? null : colorsSpinner.getSelectedItem().toString();
        if (selectedCategory == null
                || selectedType == null
                || selectedModel == null
                || selectedYear == null
                || selectedColor == null
                || platNumEditText.getText().toString().isEmpty()
                || descEditText.getText().toString().isEmpty()
                || priceEditText.getText().toString().isEmpty()
                || carImagesUris.isEmpty()) {
            return null;
        } else {
            Car car = new Car();
            car.setAgencyId(sharedPreferencesDataSource.getAgencyId());
            car.setCategory(selectedCategory.getName());
            car.setType(selectedType.getName());
            car.setModel(selectedModel.getName());
            car.setYear(selectedYear);
            car.setColor(selectedColor);
            car.setPlatNum(platNumEditText.getText().toString());
            car.setDescription(descEditText.getText().toString());
            car.setPrice(Double.parseDouble(priceEditText.getText().toString()));
            car.setStatus(Car.CarStatus.AVAILABLE.value);
            return car;
        }
    }

    @OnClick(R.id.upload_images_button)
    public void onUploadImagesClicked() {
        if (allPermissionsGranted()) {
            openGallery();
        } else if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(AddCarActivity.this, REQUIRED_PERMISSIONS, REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS);
        }
    }

    private void openGallery() {
        imagesSelector.setImagesSelectorCallback(new ImagesSelector.ImagesSelectorCallback() {
            @Override
            public void onImageAdded(Uri imageUri, int position) {
                carImagesUris.add(imageUri);
                uploadImagesEditText.setText(carImagesUris.size() + " images selected");
            }

            @Override
            public void onImageSelectedError(String error) {
                Toast.makeText(AddCarActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        imagesSelector.openImagesSelector();
    }

    private boolean allPermissionsGranted() {
        if (REQUIRED_PERMISSIONS != null) {
            for (String permission : REQUIRED_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permissions is not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}