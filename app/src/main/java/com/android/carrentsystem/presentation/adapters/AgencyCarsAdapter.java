package com.android.carrentsystem.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Car;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AgencyCarsAdapter extends RecyclerView.Adapter<AgencyCarsAdapter.CarViewHolder> {

    private final List<Car> carList;
    private final OnAgencyCarClickedCallback onAgencyCarClickedCallback;

    public AgencyCarsAdapter(List<Car> carList, OnAgencyCarClickedCallback onAgencyCarClickedCallback) {
        this.carList = carList;
        this.onAgencyCarClickedCallback = onAgencyCarClickedCallback;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agency_car_list_item_layout, parent, false);
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

        @BindView(R.id.plat_num_text_view)
        TextView platNumTextView;
        @BindView(R.id.car_status)
        TextView carStatus;
        @BindView(R.id.check_violations_button)
        Button checkViolationsButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Car car) {
            platNumTextView.setText(car.getPlatNum());
            carStatus.setText(car.getStatus());
            checkViolationsButton.setOnClickListener(v -> onAgencyCarClickedCallback.onAgencyCarClicked(car));
        }
    }

    public interface OnAgencyCarClickedCallback {
        void onAgencyCarClicked(Car car);
    }
}
