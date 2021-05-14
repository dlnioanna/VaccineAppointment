package unipi.protal.vaccineappointment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.databinding.ActivityRolesBinding;
import unipi.protal.vaccineappointment.entities.Claims;
import unipi.protal.vaccineappointment.utils.HospitalAdapter;
import unipi.protal.vaccineappointment.utils.UserRoleAdapter;

import static unipi.protal.vaccineappointment.activities.FirebaseUIActivity.CLAIMS;


public class RolesActivity extends AppCompatActivity implements UserRoleAdapter.SwitchClickListener{
    private ActivityRolesBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Claims> claimsList;
    private UserRoleAdapter appointmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRolesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.rolesRecyclerViewTitle.setText(getString(R.string.list_of_user_roles));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(CLAIMS);
        claimsList = new ArrayList<>();
        binding.firebaseProgressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Claims claims = snapshot.getValue(Claims.class);
                    claimsList.add(claims);
                }
                createAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting hospitals failed, log a message
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onSwitchClicked(int clickedItemIndex) {
        Toast.makeText(this,"clicked ",Toast.LENGTH_SHORT).show();
    }

    private void createAdapter(){
        appointmentAdapter = new UserRoleAdapter(claimsList,this);
        binding.userRolesRecyclerView.setAdapter(appointmentAdapter);
        binding.firebaseProgressBar.setVisibility(View.GONE);
    }
}