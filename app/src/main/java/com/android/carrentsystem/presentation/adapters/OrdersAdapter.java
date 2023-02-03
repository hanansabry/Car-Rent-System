package com.android.carrentsystem.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.RentOrder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final List<RentOrder> orderList;
    private final OnOrderClickedCallback onOrderClickedCallback;

    public OrdersAdapter(List<RentOrder> orderList, OnOrderClickedCallback onOrderClickedCallback) {
        this.orderList = orderList;
        this.onOrderClickedCallback = onOrderClickedCallback;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        RentOrder order = orderList.get(position);
        holder.bindData(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_id)
        TextView orderId;
        @BindView(R.id.car_desc)
        TextView carDesc;
        @BindView(R.id.phone_number)
        TextView phoneNumber;
        @BindView(R.id.from_date)
        TextView fromDate;
        @BindView(R.id.to_date)
        TextView toDate;
        @BindView(R.id.total_days)
        TextView totalDays;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.details_button)
        Button detailsButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindData(RentOrder order) {
            orderId.setText(String.format("Order Id: %s", order.getId()));
            carDesc.setText(String.format("Car: %s %s - %s",
                    order.getSelectedCar().getType(),
                    order.getSelectedCar().getModel(),
                    order.getSelectedCar().getYear())
            );
            phoneNumber.setText(String.format("Phone Number: %s", order.getPhone()));
            fromDate.setText(String.format("From: %s", formatDate(order.getFrom())));
            toDate.setText(String.format("To: %s", formatDate(order.getTo())));
            totalDays.setText(String.format(Locale.getDefault(), "(%d)", order.getNumDays()));
            status.setText(String.format("Status: %s", order.getStatus()));
            detailsButton.setOnClickListener(v -> onOrderClickedCallback.onOrderClicked(order));
        }

        private String formatDate(long milliseconds) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return simpleDateFormat.format(milliseconds);
        }
    }

    public interface OnOrderClickedCallback {
        void onOrderClicked(RentOrder order);
    }
}
