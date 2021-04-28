package unipi.protal.vaccineappointment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import unipi.protal.vaccineappointment.databinding.UserUiBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseUser;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class UserUI extends AppCompatActivity implements LocationListener {
    private UserUiBinding binding;
    private FirebaseUser user;
    private static final int REQUEST_LOCATION= 1000;
    private LocationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserUiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("currentUser");
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        binding.userName.setText(user.getDisplayName());
        binding.findHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps(v);
            }
        });

    }

    private void gps(View view){
        if(ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else{
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_LOCATION && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            }
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
