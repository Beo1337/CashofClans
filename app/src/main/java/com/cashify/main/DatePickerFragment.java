package com.cashify.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import com.cashify.base.MoneyHelper;
import com.cashify.base.Refreshable;

import java.util.Calendar;

// DatePickerFragment
// Dialog fragment to select a date which is used for the entry date filter

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    final private Calendar c = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year, month, day;
        if (MoneyHelper.isFilterSet()) {
            year = MoneyHelper.getYear();
            month = MoneyHelper.getMonth();
            day = MoneyHelper.getDay();
        } else {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        MoneyHelper.setStartDate(year, month, dayOfMonth);
        refreshFragments();
    }

    // Identify refreshable fragments and refresh them, there should be only one (refreshable)
    // fragment loaded at any time, currently no better way to identify it.
    private void refreshFragments() {
        for (Fragment f : getFragmentManager().getFragments()) {
            if (f instanceof Refreshable) ((Refreshable) f).refresh();
        }
    }
}