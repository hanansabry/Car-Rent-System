package com.android.carrentsystem.presentation.agency;

import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.RentOrder;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.viewmodels.ManageOrdersViewModel;
import com.android.carrentsystem.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

public class OrderDetailsActivity extends DaggerAppCompatActivity {

    @BindView(R.id.full_name_edit_text)
    EditText fullNameEditText;
    @BindView(R.id.civil_id_edit_text)
    EditText civilIdEditText;
    @BindView(R.id.phone_edit_text)
    EditText phoneEditText;
    @BindView(R.id.driving_license_1)
    ImageView drivingLicense1;
    @BindView(R.id.driving_license_2)
    ImageView drivingLicense2;
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
    @BindView(R.id.rent_button)
    Button rentButton;
    @BindView(R.id.agency_notes_edit_text)
    EditText agencyNotesEditText;
    @BindView(R.id.agree_client_button)
    Button agreeClientButton;
    @BindView(R.id.confirm_button)
    Button confirmAgencyButton;
    @BindView(R.id.reject_button)
    Button rejectAgencyButton;
    @BindView(R.id.from_date_edit_text)
    EditText fromDateEditText;
    @BindView(R.id.to_date_edit_text)
    EditText toDateEditText;
    @BindView(R.id.status_edit_text)
    EditText statusEditText;
    @BindView(R.id.agency_notes_layout)
    View agencyNotesView;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private ManageOrdersViewModel manageOrdersViewModel;
    private RentOrder order;
    private boolean isClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        order = getIntent().getParcelableExtra(Constants.ORDER);
        isClient = getIntent().getBooleanExtra(Constants.IS_CLIENT, false);

        setClientDetails();
        setCarDetails();
        if (isClient) {
            confirmAgencyButton.setVisibility(View.GONE);
            rejectAgencyButton.setVisibility(View.GONE);
            agencyNotesEditText.setEnabled(false);
            agencyNotesEditText.setText(order.getAgencyNotes());
            if (order.getStatus().equals(RentOrder.RentOrderStatus.PROCESSING.value)) {
                agreeClientButton.setVisibility(View.VISIBLE);
            } else if (order.getStatus().equals(RentOrder.RentOrderStatus.NEW.value)) {
                agencyNotesEditText.setHint(R.string.agency_notes_will_be_shown_here);
            }
        } else {
            if (!order.getStatus().equals(RentOrder.RentOrderStatus.NEW.value)) {
                agencyNotesView.setVisibility(View.GONE);
                confirmAgencyButton.setVisibility(View.GONE);
                rejectAgencyButton.setVisibility(View.GONE);
            }
        }
        manageOrdersViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(ManageOrdersViewModel.class);
        manageOrdersViewModel.observeOrderStatusLiveData().observe(this, success ->{
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            finish();
        });
        manageOrdersViewModel.observeErrorState().observe(this, error -> {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        });
    }

    private void setClientDetails() {
        statusEditText.setText("Order Status: " + order.getStatus());
        fullNameEditText.setText(order.getFullName());
        civilIdEditText.setText(order.getCivilId());
        phoneEditText.setText(order.getPhone());
        fromDateEditText.setText(formatDate(order.getFrom()));
        toDateEditText.setText(formatDate(order.getTo()));

        if (order.getLicenseImages().size() > 0) {
            Glide.with(this)
                    .load(order.getLicenseImages().get(0))
                    .into(drivingLicense1);
        }
        if (order.getLicenseImages().size() > 1) {
            Glide.with(this)
                    .load(order.getLicenseImages().get(1))
                    .into(drivingLicense2);
        }
    }

    private void setCarDetails() {
        rentButton.setVisibility(View.GONE);
        priceLbl.setText("/Total Price");
        totalPriceView.setText(String.valueOf(order.getTotalPrice()));
        category.setText(order.getSelectedCar().getCategory());
        model.setText(order.getSelectedCar().getModel());
        year.setText(order.getSelectedCar().getYear());
        description.setText(order.getSelectedCar().getDescription());
        agencyName.setText(order.getSelectedCar().getAgencyName());
        carColor.setBackgroundColor(Color.parseColor(order.getSelectedCar().getColor()));
        Glide.with(this)
                .load(order.getSelectedCar().getCarImagesUrls().get(0))
                .into(carImage);
    }

    @OnClick(R.id.confirm_button)
    public void onConfirmClicked() {

        String agencyNotes = agencyNotesEditText.getText().toString();
        if (agencyNotes.isEmpty()) {
            Toast.makeText(this, "You must add your notes", Toast.LENGTH_SHORT).show();
        } else {
            manageOrdersViewModel.addStatusToRentOrder(order.getId(), agencyNotes, true);
        }
    }

    @OnClick(R.id.reject_button)
    public void onRejectClicked() {
        String agencyNotes = agencyNotesEditText.getText().toString();
        manageOrdersViewModel.addStatusToRentOrder(order.getId(), agencyNotes, false);
    }

    @OnClick(R.id.agree_client_button)
    public void onAgreeClientClicked() {
        //change order status to confirmed
        //change car status to rented
        manageOrdersViewModel.confirmRentOrder(order);
//        manageOrdersViewModel.addStatusToRentOrder(order.getId(), agencyNotes, false);
    }

    private String formatDate(long milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return simpleDateFormat.format(milliseconds);
    }


    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}