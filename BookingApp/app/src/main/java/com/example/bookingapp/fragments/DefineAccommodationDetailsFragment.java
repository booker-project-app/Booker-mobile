package com.example.bookingapp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookingapp.FragmentTransition;
import com.example.bookingapp.R;
import com.example.bookingapp.databinding.FragmentDefineAccommodationDetailsBinding;

import java.util.Calendar;

public class DefineAccommodationDetailsFragment extends Fragment {

    private FragmentDefineAccommodationDetailsBinding binding;

    public static DefineAccommodationDetailsFragment newInstance() {
        Bundle args = new Bundle();

        DefineAccommodationDetailsFragment fragment = new DefineAccommodationDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDefineAccommodationDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setupDatePickers();
        setupFragmentTransition();
        return root;
    }

    private void setupFragmentTransition() {
        EditText until = binding.until;
        until.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT) {
                    FragmentTransition.to(DefineAccommodationPriceFragment.newInstance(), requireActivity(), false, R.id.fragment_placeholder);
                    return true;
                }
                return false;
            }
        });
    }

    private void setupDatePickers() {
        EditText from = binding.from;
        from.setOnClickListener(v -> showDatePickerDialog(from));

        EditText until = binding.until;
        until.setOnClickListener(v -> showDatePickerDialog(until));
    }

    private void showDatePickerDialog(EditText targetEditText) {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String newDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        targetEditText.setText(newDate);
                    }
                }, year, month, day);
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}