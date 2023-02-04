package com.android.carrentsystem.presentation.agency;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.carrentsystem.R;
import com.android.carrentsystem.data.models.Violation;
import com.android.carrentsystem.presentation.adapters.ViolationsAdapter;
import com.android.carrentsystem.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViolationListBottomSheetDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViolationListBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "ViolationListBottomSheetDialog";
    private ArrayList<Violation> violationList;
    @BindView(R.id.violation_list_recycler_view)
    RecyclerView violationsRecyclerView;

    public ViolationListBottomSheetDialog() {
        // Required empty public constructor
    }

    public static ViolationListBottomSheetDialog newInstance(ArrayList<Violation> violationList) {
        ViolationListBottomSheetDialog bottomSheetDialog =  new ViolationListBottomSheetDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.VIOLATION_LIST, violationList);
        bottomSheetDialog.setArguments(args);
        return bottomSheetDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            violationList = getArguments().getParcelableArrayList(Constants.VIOLATION_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_violation_list_bottom_sheet_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViolationsAdapter violationsAdapter = new ViolationsAdapter(violationList);
        violationsRecyclerView.setAdapter(violationsAdapter);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        dismiss();
    }
}