package com.example.lucifer.androidappforcrimereporting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Lucifer on 2/6/2018.
 */

public class DialogBoxes {


   public Context context;

    public  DialogBoxes(Context context)
    {
        this.context=context;
    }

    public  void timepicker()
    {

        final Calendar c = Calendar.getInstance();
      final int   mHour = c.get(Calendar.HOUR_OF_DAY);
       int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(DialogBoxes.this.context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        TextView textView=(TextView)((Activity)context).findViewById(R.id.Time);
                        textView.setText(hourOfDay+":"+minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    public void Date() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
      int   mMonth = c.get(Calendar.MONTH);
       int  mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(   DialogBoxes.this.context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        TextView date=(TextView)((Activity)context).findViewById(R.id.Date1);


                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}
