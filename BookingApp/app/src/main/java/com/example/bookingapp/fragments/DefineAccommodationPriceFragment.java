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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bookingapp.FragmentTransition;
import com.example.bookingapp.R;
import com.example.bookingapp.databinding.FragmentDefineAccommodationPriceBinding;
import com.example.bookingapp.enums.PriceType;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class DefineAccommodationPriceFragment extends Fragment {

   private FragmentDefineAccommodationPriceBinding binding;

   public static DefineAccommodationPriceFragment newInstance() {

       Bundle args = new Bundle();

       DefineAccommodationPriceFragment fragment = new DefineAccommodationPriceFragment();
       fragment.setArguments(args);
       return fragment;
   }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDefineAccommodationPriceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setupDatePickers();
        setupPriceTypeSpinner();
        setupFragmentTransition();
        return root;
    }

    private void setupFragmentTransition() {
       EditText price = binding.newPrice;

       price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_NEXT) {
                   FragmentTransition.to(DefineCancellationDeadlineFragment.newInstance(), requireActivity(), false, R.id.fragment_placeholder);
                   return true;
               }
               return false;
           }
       });
    }

    private void setupPriceTypeSpinner() {
        Spinner spinner = binding.spinner;
        List<PriceType> enumValues = Arrays.asList(PriceType.values());

        ArrayAdapter<PriceType> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                enumValues
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupDatePickers() {
       EditText from = binding.from;
       from.setOnClickListener(v -> showDatePickerDialog(from));

       EditText until = binding.until;
       until.setOnClickListener(v -> showDatePickerDialog(until));
    }

    private void showDatePickerDialog(EditText targetEditText) {
        Calendar c = Calendar.getInstance();

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