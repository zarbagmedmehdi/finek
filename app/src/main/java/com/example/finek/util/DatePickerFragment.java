package com.example.finek.util;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.finek.CreateTaskActivity;
import com.example.finek.CreateTaskActivity;
import com.example.finek.DeclarerPerduActivity;

import java.util.Calendar;
public  class DatePickerFragment extends DialogFragment
                            implements DatePickerDialog.OnDateSetListener {
Button dateBtn;
int cas;

    public DatePickerFragment(Button dateBtn ,int cas) {
        this.dateBtn = dateBtn;
        this.cas=cas;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int m=month+1;
        dateBtn.setText(year+"/"+m+"/"+day);
        if(cas==1) {
            DeclarerPerduActivity.tabDate[0] = year;
            DeclarerPerduActivity.tabDate[1] = m;
            DeclarerPerduActivity.tabDate[2] = day;
        }
        if(cas==2){
            CreateTaskActivity.tabDate[0] = year;
            CreateTaskActivity.tabDate[1] = m;
            CreateTaskActivity.tabDate[2] = day;
        }

    }
}