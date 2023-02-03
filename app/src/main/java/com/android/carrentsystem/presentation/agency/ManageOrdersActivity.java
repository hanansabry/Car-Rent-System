package com.android.carrentsystem.presentation.agency;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.carrentsystem.R;
import com.android.carrentsystem.datasource.SharedPreferencesDataSource;
import com.android.carrentsystem.di.ViewModelProviderFactory;
import com.android.carrentsystem.presentation.adapters.OrdersAdapter;
import com.android.carrentsystem.presentation.viewmodels.ManageOrdersViewModel;
import com.android.carrentsystem.utils.Constants;

import javax.inject.Inject;

public class ManageOrdersActivity extends DaggerAppCompatActivity {

    @BindView(R.id.orders_recycler_view)
    RecyclerView ordersRecyclerView;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    private ManageOrdersViewModel manageOrdersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);
        ButterKnife.bind(this);

        manageOrdersViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(ManageOrdersViewModel.class);
        manageOrdersViewModel.retrieveAgencyOrder(sharedPreferencesDataSource.getAgencyId());
        manageOrdersViewModel.observeOrderListLiveDate().observe(this, orderList -> {
            if (orderList != null && !orderList.isEmpty()) {
                OrdersAdapter ordersAdapter = new OrdersAdapter(orderList, order -> {
                    Intent intent = new Intent(this, OrderDetailsActivity.class);
                    intent.putExtra(Constants.ORDER, order);
                    startActivity(intent);
                });
                ordersRecyclerView.setAdapter(ordersAdapter);
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