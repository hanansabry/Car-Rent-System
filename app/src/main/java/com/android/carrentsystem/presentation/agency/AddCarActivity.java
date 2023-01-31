package com.android.carrentsystem.presentation.agency;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.widget.Toast;

import com.android.carrentsystem.R;

public class AddCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.submit_button)
    public void onSubmitClicked() {
        Toast.makeText(this, "new car is added", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick(R.id.upload_images_button)
    public void onUploadImagesClicked() {
        Toast.makeText(this, "upload Images", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}