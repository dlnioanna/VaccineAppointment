package unipi.protal.vaccineappointment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import unipi.protal.vaccineappointment.databinding.ActivityHospitalListBinding;
import unipi.protal.vaccineappointment.entities.Hospital;
import unipi.protal.vaccineappointment.utils.HospitalAdapter;

import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.VACCINE_POINTS;

public class HospitalListActivity extends AppCompatActivity implements HospitalAdapter.ListItemClickListener  {
    private ActivityHospitalListBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Hospital> hospitalList;
    private HospitalAdapter hospitalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(VACCINE_POINTS);
        hospitalList = new ArrayList<>();
        getHospitalList();

    }

    private void getHospitalList() {
        binding.firebaseProgressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hospital hospital = snapshot.getValue(Hospital.class);
                    hospitalList.add(hospital);
                    createAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting hospitals failed, log a message
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void createAdapter() {
        hospitalAdapter = new HospitalAdapter(hospitalList, this);
        binding.hospitalRecyclerView.setAdapter(hospitalAdapter);
        binding.firebaseProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent i = new Intent(this, AppointmentListActivity.class);
        i.putExtra("hospital", hospitalList.get(clickedItemIndex));
        startActivity(i);
    }
}