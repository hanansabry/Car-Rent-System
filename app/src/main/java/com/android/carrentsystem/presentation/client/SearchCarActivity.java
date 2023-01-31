package com.android.carrentsystem.presentation.client;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.android.carrentsystem.R;

public class SearchCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.search_cars_button)
    public void onSearchCarsClicked() {
        startActivity(new Intent(this, SearchResultsActivity.class));
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}