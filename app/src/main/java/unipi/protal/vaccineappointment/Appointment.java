package unipi.protal.vaccineappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import unipi.protal.vaccineappointment.databinding.ActivityAppointmentBinding;

public class Appointment extends AppCompatActivity {
    private ActivityAppointmentBinding binding;
    private static final int DAYS_BETWEEN_DOSES = 28;
    private String dateDoseOne,dateDoseTwo,getDateDoseTwo,timeDoseOne, finalDateOne,finalDateTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Hospital hospital = getIntent().getParcelableExtra("hospital");
        Log.e("hospital selected is ", hospital.getTitle());
        binding.datePickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        dateDoseOne=String.format("%02d-%02d-%04d", selectedday ,selectedmonth , selectedyear);
                        binding.selectedDate.setText(String.format("%02d-%02d-%04d", selectedday ,selectedmonth , selectedyear));
                        if(timeDoseOne!=null){
                            binding.firstDose.setText(getString(R.string.first_dose)+dateDoseOne+" και ώρα "+timeDoseOne);
                            binding.firstDose.setVisibility(View.VISIBLE);
                        }
                        mcurrentDate.add(Calendar.DAY_OF_MONTH, DAYS_BETWEEN_DOSES);
                        dateDoseTwo=String.format("%02d-%02d-%04d", mcurrentDate.get(Calendar.DAY_OF_MONTH) ,mcurrentDate.get(Calendar.MONTH)
                                , mcurrentDate.get(Calendar.YEAR));
                        if(timeDoseOne!=null){
                            binding.secondDose.setText(getString(R.string.second_dose)+dateDoseTwo+" και ώρα "+timeDoseOne);
                            binding.secondDose.setVisibility(View.VISIBLE);
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setCanceledOnTouchOutside(true);
                mDatePicker.show();
            }
        });
   binding.timePickerImage.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Calendar mcurrentTime = Calendar.getInstance();
           int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
           int minute = mcurrentTime.get(Calendar.MINUTE);
           TimePickerDialog mTimePicker= new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
               @Override
               public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                   timeDoseOne=String.format("%02d:%02d", selectedHour, selectedMinute);
                   binding.selectedTime.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                   if(dateDoseOne!=null){
                       binding.firstDose.setText(getString(R.string.first_dose)+dateDoseOne+" και ώρα "+timeDoseOne);
                       binding.firstDose.setVisibility(View.VISIBLE);
                       binding.secondDose.setText(getString(R.string.second_dose)+dateDoseTwo+" και ώρα "+timeDoseOne);
                       binding.secondDose.setVisibility(View.VISIBLE);
                   }
               }
           }, hour, minute, true);//Yes 24 hour time
           mTimePicker.setCanceledOnTouchOutside(true);
           mTimePicker.show();
       }
   });
    }
}