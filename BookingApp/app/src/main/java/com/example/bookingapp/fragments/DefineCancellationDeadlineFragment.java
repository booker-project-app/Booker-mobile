package com.example.bookingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.bookingapp.FragmentTransition;
import com.example.bookingapp.R;
import com.example.bookingapp.databinding.FragmentDefineCancellationDeadlineBinding;

public class DefineCancellationDeadlineFragment extends Fragment {

    private FragmentDefineCancellationDeadlineBinding binding;

    public static DefineCancellationDeadlineFragment newInstance() {

        Bundle args = new Bundle();

        DefineCancellationDeadlineFragment fragment = new DefineCancellationDeadlineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDefineCancellationDeadlineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setupNumberPickerDialog();
        setupSubmitButtonListener();
        return root;
    }

    private void setupSubmitButtonListener() {
        Button btnSubmit = binding.btnSubmit;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Accommodation details successfully updated", Toast.LENGTH_SHORT).show();
                FragmentTransition.to(AccommodationListingFragment.newInstance(), requireActivity(), false, R.id.fragment_placeholder);
            }
        });
    }

    private void setupNumberPickerDialog() {
        EditText deadline = binding.cancellationDeadline;
        deadline.setOnClickListener(v -> showNumberPickerDialog(deadline));
    }

    private void showNumberPickerDialog(EditText targetEditText) {
        NumberPickerDialog dialog = new NumberPickerDialog();

        dialog.setValueChangeListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                targetEditText.setText(String.valueOf(newVal));
            }
        });

        dialog.show(getChildFragmentManager(), "numberPicker");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}