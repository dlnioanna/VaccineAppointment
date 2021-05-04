package unipi.protal.vaccineappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import unipi.protal.vaccineappointment.databinding.ActivityAppointmentBinding;

public class Appointment extends AppCompatActivity {
    private ActivityAppointmentBinding binding;
    private static final int DAYS_BETWEEN_DOSES=28;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Hospital hospital = getIntent().getParcelableExtra("hospital");
        Log.e("hospital selected is ",hospital.getTitle());
        binding.calendarView.setMinDate(System.currentTimeMillis());
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.e("date selected is ",dayOfMonth+"-"+month+"-"+year);
//                Calendar cal = Calendar.getInstance();
//                cal.add(dayOfMonth, DAYS_BETWEEN_DOSES);
//                Log.e("date 2 is ",cal.getTime().toString());
            }
        });
    }
}