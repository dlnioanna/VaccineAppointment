package unipi.protal.vaccineappointment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.util.Calendar;

import unipi.protal.vaccineappointment.entities.Appointment;
import unipi.protal.vaccineappointment.entities.Hospital;
import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.databinding.ActivityAppointmentBinding;

import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.APPOINTMENTS;
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
        hospital = getIntent().getParcelableExtra("hospital");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(VACCINE_POINTS);
        user = firebaseAuth.getCurrentUser();
        binding.appointmentHospitalTitle.setText(hospital.getTitle());
        binding.datePickerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar c = Calendar.getInstance();
                        c.set(selectedyear, selectedmonth, selectedday);
                        dateDoseOne = String.format("%02d-%02d-%04d", selectedday, selectedmonth, selectedyear);
                        binding.selectedDate.setText(String.format("%02d-%02d-%04d", selectedday, selectedmonth, selectedyear));
                        if (timeDoseOne != null) {
                            binding.firstDose.setText(getString(R.string.first_dose) + dateDoseOne + " και ώρα " + timeDoseOne);
                            binding.firstDose.setVisibility(View.VISIBLE);
                        }
                        c.add(Calendar.DAY_OF_MONTH, DAYS_BETWEEN_DOSES);
                        dateDoseTwo = String.format("%02d-%02d-%04d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)
                                , c.get(Calendar.YEAR));
                        if (timeDoseOne != null) {
                            binding.secondDose.setText(getString(R.string.second_dose) + dateDoseTwo + " και ώρα " + timeDoseOne);
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
                TimePickerDialog mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeDoseOne = String.format("%02d:%02d", selectedHour, selectedMinute);
                        binding.selectedTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        if (dateDoseOne != null) {
                            binding.firstDose.setText(getString(R.string.first_dose) + dateDoseOne + " και ώρα " + timeDoseOne);
                            binding.firstDose.setVisibility(View.VISIBLE);
                            binding.secondDose.setText(getString(R.string.second_dose) + dateDoseTwo + " και ώρα " + timeDoseOne);
                            binding.secondDose.setVisibility(View.VISIBLE);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setCanceledOnTouchOutside(true);
                mTimePicker.show();
            }
        });
        binding.saveAppointment.setOnClickListener(v -> saveApointment());
    }

    private void saveApointment() {
        if (checkFields()) {
            Query query = databaseReference.orderByChild("title").equalTo(hospital.getTitle());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"
                            hospitalId = snapshot.getKey();
                        }
                        Appointment appointment = new Appointment(binding.nameAppointment.getText().toString(), binding.lastNameAppointment.getText().toString(),
                                binding.telephoneAppointment.getText().toString(), dateDoseOne, timeDoseOne, dateDoseTwo);
                        databaseReference.child(hospitalId).child(APPOINTMENTS).child(user.getUid()).setValue(appointment);
                    }
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("f_user", user);
        outState.putString("name", binding.nameAppointment.getText().toString());
        outState.putString("last_name", binding.lastNameAppointment.getText().toString());
        outState.putString("telephone", binding.telephoneAppointment.getText().toString());
        outState.putString("date", dateDoseOne);
        outState.putString("time", timeDoseOne);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getParcelable("f_user");
        binding.nameAppointment.setText(savedInstanceState.getString("name"));
        binding.lastNameAppointment.setText(savedInstanceState.getString("last_name"));
        binding.telephoneAppointment.setText(savedInstanceState.getString("telephone"));
        dateDoseOne = savedInstanceState.getString("dateDoseOne");
        timeDoseOne = savedInstanceState.getString("time");
    }
}