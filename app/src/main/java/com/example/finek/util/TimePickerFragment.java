package com.example.finek.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.finek.CreateTaskActivity;
import com.example.finek.DeclarerPerduActivity;

import java.util.Calendar;


 public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
Button timeBtn;
int cas;

    public TimePickerFragment(Button timeBtn,int cas) {
        this.timeBtn = timeBtn;
        this.cas=cas;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

timeBtn.setText( hourOfDay+":"+minute+" H");
if(cas==1) {
    DeclarerPerduActivity.tabDate[3] = hourOfDay;
    DeclarerPerduActivity.tabDate[4] = minute;
    DeclarerPerduActivity.tabDate[5] = 0;
}
if(cas==2){
    CreateTaskActivity.tabDate[3] = hourOfDay;
    CreateTaskActivity.tabDate[4] = minute;
    CreateTaskActivity.tabDate[5] = 0;
}



    }
}