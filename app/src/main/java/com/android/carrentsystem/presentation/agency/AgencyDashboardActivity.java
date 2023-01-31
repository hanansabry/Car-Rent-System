package com.android.carrentsystem.presentation.agency;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.android.carrentsystem.R;

public class AgencyDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_dashboard);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_new_car_view)
    public void onAddNewCarClicked() {
        startActivity(new Intent(this, AddCarActivity.class));
    }

    @OnClick(R.id.car_list_view)
    public void onCarLisClicked() {
        startActivity(new Intent(this, CarListActivity.class));
    }

    @OnClick(R.id.manage_orders_view)
    public void onManageOrdersClicked() {
        startActivity(new Intent(this, ManageOrdersActivity.class));
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}