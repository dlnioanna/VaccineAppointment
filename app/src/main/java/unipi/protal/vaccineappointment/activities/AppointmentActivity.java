package unipi.protal.vaccineappointment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.util.Calendar;
import unipi.protal.vaccineappointment.entities.Appointment;
import unipi.protal.vaccineappointment.entities.Hospital;
import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.databinding.ActivityAppointmentBinding;
import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.APPOINTMENTS;
import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.TITLE;
import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.VACCINE_POINTS;

public class AppointmentActivity extends AppCompatActivity {
    private ActivityAppointmentBinding binding;
    private static final int DAYS_BETWEEN_DOSES = 28;
    private String dateDoseOne, dateDoseTwo, getDateDoseTwo, timeDoseOne, finalDateOne, finalDateTwo;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private Hospital hospital;
    private String hospitalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // get selected hospital from previous activity
        hospital = getIntent().getParcelableExtra("hospital");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(VACCINE_POINTS);
        user = firebaseAuth.getCurrentUser();
        binding.appointmentHospitalTitle.setText(hospital.getTitle());
        /*
        on click calendar window opens to select date
        after user selects date for first dose, the date for second dose is automatically
        created after 28 days
         */
        binding.datePickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(selectedyear, selectedmonth+1, selectedday);
                        dateDoseOne = String.format("%02d-%02d-%04d", selectedday, selectedmonth+1, selectedyear);
                        binding.selectedDate.setText(String.format("%02d-%02d-%04d", selectedday, selectedmonth+1, selectedyear));
                        if (timeDoseOne != null) {
                            binding.firstDose.setText(getString(R.string.first_dose) + dateDoseOne + " και ώρα " + timeDoseOne);
                            binding.dateOneLinearLayout.setVisibility(View.VISIBLE);
                        }
                        calendar.add(Calendar.DAY_OF_MONTH, DAYS_BETWEEN_DOSES);
                        dateDoseTwo = String.format("%02d-%02d-%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)
                                , calendar.get(Calendar.YEAR));
                        if (timeDoseOne != null) {
                            binding.secondDose.setText(getString(R.string.second_dose) + dateDoseTwo + " και ώρα " + timeDoseOne);
                            binding.dateTwoLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.setCanceledOnTouchOutside(true);
                mDatePicker.show();
            }
        });
     /*
        on click clock window opens to select time
        after user selects time for first dose, the time for second dose is automatically
        created as the same time
         */
        binding.timePickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeDoseOne = String.format("%02d:%02d", selectedHour, selectedMinute);
                        binding.selectedTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        if (dateDoseOne != null) {
                            // only after first and second doses are defined they are shown
                            binding.firstDose.setText(getString(R.string.first_dose) + dateDoseOne + " και ώρα " + timeDoseOne);
                            binding.dateOneLinearLayout.setVisibility(View.VISIBLE);
                            binding.secondDose.setText(getString(R.string.second_dose) + dateDoseTwo + " και ώρα " + timeDoseOne);
                            binding.dateTwoLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setCanceledOnTouchOutside(true);
                mTimePicker.show();
            }
        });
        binding.saveAppointment.setOnClickListener(v -> saveApointment());
    }

    // when user saves appointment it is stored on database
    private void saveApointment() {
        if (checkFields()) {
            // get the selected hospital and retrieve appointments node to save user's appointment
            Query query = databaseReference.orderByChild(TITLE).equalTo(hospital.getTitle());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            hospitalId = snapshot.getKey();
                        }
                        Appointment appointment = new Appointment(binding.nameAppointment.getText().toString(), binding.lastNameAppointment.getText().toString(),
                                binding.telephoneAppointment.getText().toString(), dateDoseOne, timeDoseOne, dateDoseTwo);
                        databaseReference.child(hospitalId).child(APPOINTMENTS).child(user.getUid()).setValue(appointment);
                    }
                    // after saving appointment return to previous activity
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    // method used to check if user has filled requested data
    private boolean checkFields() {
        if (binding.nameAppointment.getText() != null && !binding.nameAppointment.getText().equals("")
                && binding.lastNameAppointment.getText() != null && !binding.lastNameAppointment.getText().equals("")
                && binding.telephoneAppointment.getText() != null && !binding.telephoneAppointment.getText().equals("")
                && dateDoseOne != null && timeDoseOne != null) {
            return true;
        } else {
            Toast.makeText(this, getString(R.string.save_appointment_error_message), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // save data to prevent losing them on screen rotation when app is running but not shown
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("f_user", user);
        outState.putString("name", binding.nameAppointment.getText().toString());
        outState.putString("last_name", binding.lastNameAppointment.getText().toString());
        outState.putString("telephone", binding.telephoneAppointment.getText().toString());
        outState.putString("dateDoseOne", dateDoseOne);
        outState.putString("time", timeDoseOne);
        super.onSaveInstanceState(outState);
    }

    // restore data that have been saved on onSaveInstanceState

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getParcelable("f_user");
        binding.nameAppointment.setText(savedInstanceState.getString("name"));
        binding.lastNameAppointment.setText(savedInstanceState.getString("last_name"));
        binding.telephoneAppointment.setText(savedInstanceState.getString("telephone"));
        dateDoseOne = savedInstanceState.getString("dateDoseOne");
        timeDoseOne = savedInstanceState.getString("time");
        binding.selectedDate.setText(dateDoseOne);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        try {
            calendar.setTime(sdf.parse(timeDoseOne));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_MONTH, DAYS_BETWEEN_DOSES);
        dateDoseTwo = String.format("%02d-%02d-%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.YEAR));
        binding.selectedTime.setText(timeDoseOne);
        // only after first and second doses are defined they are shown
        if ((dateDoseOne != null)&&(timeDoseOne != null)) {
            binding.firstDose.setText(getString(R.string.first_dose) + dateDoseOne + " και ώρα " + timeDoseOne);
            binding.dateOneLinearLayout.setVisibility(View.VISIBLE);
            binding.secondDose.setText(getString(R.string.second_dose) + dateDoseTwo + " και ώρα " + timeDoseOne);
            binding.dateTwoLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}