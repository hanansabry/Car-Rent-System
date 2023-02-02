package com.android.carrentsystem.presentation.client;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.RentOrder;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.images.GalleryImagesSelector;
import com.android.carrentsystem.presentation.images.ImagesSelector;
import com.android.carrentsystem.presentation.viewmodels.RentCarViewModel;
import com.android.carrentsystem.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class RentCarOrderActivity extends DaggerAppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;
    @BindView(R.id.rent_button)
    Button rentButton;
    @BindView(R.id.rules_check_box)
    CheckBox rulesCheckBox;
    @BindView(R.id.full_name_edit_text)
    EditText fullNameEditText;
    @BindView(R.id.civil_id_edit_text)
    EditText civilIdEditText;
    @BindView(R.id.phone_edit_text)
    EditText phoneEditText;
    @BindView(R.id.driving_license_edit_text)
    EditText uploadDrivingLicenseEditText;
    @BindView(R.id.price)
    TextView totalPriceView;
    @BindView(R.id.price_lbl)
    TextView priceLbl;
    @BindView(R.id.car_category)
    TextView category;
    @BindView(R.id.car_model)
    TextView model;
    @BindView(R.id.model_year)
    TextView year;
    @BindView(R.id.car_color)
    ShapeableImageView carColor;
    @BindView(R.id.car_image)
    ImageView carImage;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.agency_name)
    TextView agencyName;

    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private RentCarViewModel rentCarViewModel;
    private List<Uri> licenseImages = new ArrayList<>();
    private List<String> uploadedLicenseImagesUris = new ArrayList<>();
    private GalleryImagesSelector imagesSelector;
    private ProgressDialog loadingProgressDialog;
    private Car selectedCar;
    private long from;
    private long to;
    private double totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car_order);
        ButterKnife.bind(this);

        loadingProgressDialog = new ProgressDialog(this);
        loadingProgressDialog.setMessage("Loading");

        selectedCar = getIntent().getParcelableExtra(Constants.CAR);
        from = getIntent().getLongExtra(Constants.FROM, 0);
        to = getIntent().getLongExtra(Constants.TO, 0);
        long dayNum = getIntent().getLongExtra(Constants.NUM_DAYS, 0);

        setCarDetails(dayNum);
        imagesSelector = new GalleryImagesSelector(this);
        rentCarViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(RentCarViewModel.class);
    }

    private void setCarDetails(long dayNum) {
        rentButton.setVisibility(View.GONE);
        priceLbl.setText("/Total Price");
        totalPrice = dayNum * selectedCar.getPrice();
        totalPriceView.setText(String.valueOf(totalPrice));
        category.setText(selectedCar.getCategory());
        model.setText(selectedCar.getModel());
        year.setText(selectedCar.getYear());
        description.setText(selectedCar.getDescription());
        agencyName.setText(selectedCar.getAgencyName());
        totalPriceView.setText(String.valueOf(selectedCar.getPrice()));
        carColor.setBackgroundColor(Color.parseColor(selectedCar.getColor()));
        Glide.with(this)
                .load(selectedCar.getCarImagesUrls().get(0))
                .into(carImage);
    }

    @OnCheckedChanged(R.id.rules_check_box)
    public void onRulesCheckBoxClicked(boolean isChecked) {
        if (isChecked) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Agency Rent Rules");
            builder.setMessage(getString(R.string.agency_rules));
            builder.setPositiveButton(R.string.agree, (dialog, which) -> rulesCheckBox.setChecked(true));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @OnClick(R.id.submit_button)
    public void onSubmitClicked() {
        RentOrder newOrder = validateOrderProperties();
        if (newOrder != null) {
            loadingProgressDialog.show();
            // add new car to database
            //upload carImagesFirstly
            for (Uri imageUri : licenseImages) {
                rentCarViewModel
                        .uploadImageToFirebaseStorage(
                                imageUri,
                                selectedCar.getAgencyId(),
                                newOrder.getCivilId()
                        );
            }
            rentCarViewModel.observeUploadImageState().observe(this, url -> {
                uploadedLicenseImagesUris.add(url);
                if (licenseImages.size() == uploadedLicenseImagesUris.size()) {
                    newOrder.setLicenseImages(uploadedLicenseImagesUris);
                    rentCarViewModel.addNewOrder(newOrder);
                    rentCarViewModel.observeUploadImageState().observe(this, success -> {
                        loadingProgressDialog.dismiss();
                        Toast.makeText(this, "New order is added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        } else {
            if (!rulesCheckBox.isChecked()) {
                Toast.makeText(this, R.string.agency_rules_must_be_agreed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.all_fields_required_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private RentOrder validateOrderProperties() {
        if (fullNameEditText.getText().toString().isEmpty()
                || civilIdEditText.getText().toString().isEmpty()
                || phoneEditText.getText().toString().isEmpty()
                || licenseImages.isEmpty()
                || !rulesCheckBox.isChecked()) {
            return null;
        } else {
            RentOrder order = new RentOrder();
            order.setAgencyId(selectedCar.getAgencyId());
            order.setFullName(fullNameEditText.getText().toString());
            order.setCivilId(civilIdEditText.getText().toString());
            order.setPhone(phoneEditText.getText().toString());
            order.setStatus(RentOrder.RentOrderStatus.NEW.value);
            order.setSelectedCar(selectedCar);
            order.setFrom(from);
            order.setTo(to);
            order.setTotalPrice(totalPrice);
            return order;
        }
    }

    @OnClick(R.id.upload_license_button)
    public void onUploadLicenseClicked() {
        if (allPermissionsGranted()) {
            openGallery();
        } else if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(RentCarOrderActivity.this, REQUIRED_PERMISSIONS, REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS);
        }
    }

    private void openGallery() {
        imagesSelector.setImagesSelectorCallback(new ImagesSelector.ImagesSelectorCallback() {
            @Override
            public void onImageAdded(Uri imageUri, int position) {
                licenseImages.add(imageUri);
                uploadDrivingLicenseEditText.setText(licenseImages.size() + " images selected");
            }

            @Override
            public void onImageSelectedError(String error) {
                Toast.makeText(RentCarOrderActivity.this, error, Toast.LENGTH_SHORT).show();
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