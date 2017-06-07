package com.cashify.base;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        if (MoneyHelper.isFilterSet()){
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

    private void refreshFragments() {
        for (Fragment f :  getFragmentManager().getFragments()){
            if (f instanceof Refreshable) {
                Log.e("", f.toString());
                ((Refreshable) f).refresh();
            }
        }
    }
}