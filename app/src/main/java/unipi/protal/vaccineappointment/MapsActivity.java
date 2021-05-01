package unipi.protal.vaccineappointment;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import unipi.protal.vaccineappointment.databinding.ActivityFirebaseUiBinding;
import unipi.protal.vaccineappointment.databinding.ActivityMapsBinding;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, HospitalAdapter.ListItemClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private boolean mapReady = false;
    private HospitalAdapter hospitalAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("vaccine_point");
        databaseReference.setValue("hospitalList");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        binding.btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        binding.btnHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
         hospitalAdapter = new HospitalAdapter( 10,this);
         binding.hospitalRecyclerView.setAdapter(hospitalAdapter);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this, "success "+clickedItemIndex, Toast.LENGTH_LONG).show();

    }
}