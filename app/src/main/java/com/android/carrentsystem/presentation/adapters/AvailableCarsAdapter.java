package com.android.carrentsystem.presentation.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AvailableCarsAdapter extends RecyclerView.Adapter<AvailableCarsAdapter.CarViewHolder> {

    private List<Car> carList;
    private SelectCarCallback selectCarCallback;

    public AvailableCarsAdapter(List<Car> carList, SelectCarCallback selectCarCallback) {
        this.carList = carList;
        this.selectCarCallback = selectCarCallback;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.bindData(car);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.rent_button)
        Button rentButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindData(Car car) {
            category.setText(car.getCategory());
            model.setText(car.getModel());
            year.setText(car.getYear());
            description.setText(car.getDescription());
            agencyName.setText(car.getAgencyName());
            price.setText(String.valueOf(car.getPrice()));
            carColor.setBackgroundColor(Color.parseColor(car.getColor()));
            Glide.with(itemView.getContext())
                    .load(car.getCarImagesUrls().get(0))
                    .into(carImage);
            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCarCallback.onCarSelected(car);
                }
            });
        }
    }

    public interface SelectCarCallback {
        void onCarSelected(Car car);
    }
}
