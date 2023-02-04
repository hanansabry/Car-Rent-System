package com.android.carrentsystem.presentation.adapters;

import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Violation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViolationsAdapter extends RecyclerView.Adapter<ViolationsAdapter.ViolationViewHolder> {

    private final List<Violation> violationList;

    public ViolationsAdapter(List<Violation> violationList) {
        this.violationList = violationList;
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.violation_item_layout, parent, false);
        return new ViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationViewHolder holder, int position) {
        Violation violation = violationList.get(position);
        holder.bindData(violation);
    }

    @Override
    public int getItemCount() {
        return violationList.size();
    }

    class ViolationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.violations_notes)
        TextView violationNotes;
        @BindView(R.id.cost)
        TextView cost;
        @BindView(R.id.date)
        TextView date;

        public ViolationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Violation violation) {
            violationNotes.setText(violation.getNotes());
            cost.setText(String.format("Cost: %s", violation.getCost()));
            date.setText(violation.getDate());
        }
    }
}
