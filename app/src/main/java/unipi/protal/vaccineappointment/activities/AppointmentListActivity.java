package unipi.protal.vaccineappointment.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.databinding.ActivityAppointmentListBinding;
import unipi.protal.vaccineappointment.entities.Appointment;
import unipi.protal.vaccineappointment.entities.Hospital;
import unipi.protal.vaccineappointment.utils.AppointmentAdapter;
import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.APPOINTMENTS;
import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.TITLE;
import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.VACCINE_POINTS;


public class AppointmentListActivity extends AppCompatActivity{
    private Appointment appointment;
    private Hospital hospital;
    private String appointmentId;
    private ActivityAppointmentListBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Appointment> appointmentList;
    private AppointmentAdapter appointmentAdapter;
    private String hospitalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        hospital = getIntent().getParcelableExtra("hospital");
        binding.hospitalAppointmentRecyclerViewTitle.setText(getString(R.string.list_of_appointments_title)+" "+hospital.getTitle());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(VACCINE_POINTS);
        /*
        query the database to get the uid of the hospital selected on previous activity
         */
        Query query = databaseReference.orderByChild(TITLE).equalTo(hospital.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        hospitalId = snapshot.getKey();
                        getAppointmentList(hospitalId);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        appointmentList = new ArrayList<>();

    }
// read all appointments from db and populate adapter
    private void getAppointmentList(String id) {
        binding.firebaseProgressBar.setVisibility(View.VISIBLE);
        databaseReference.child(id).child(APPOINTMENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    appointmentList.add(appointment);
                }  createAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting hospitals failed, show a message
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // populate AppointmentAdapter with data from database and load ui
    private void createAdapter() {
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        binding.appointmentRecyclerView.setAdapter(appointmentAdapter);
        binding.firebaseProgressBar.setVisibility(View.GONE);
    }

}