package unipi.protal.vaccineappointment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.databinding.ActivityFirebaseUiBinding;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FirebaseUIActivity extends AppCompatActivity implements LocationListener {
    private static final int RC_SIGN_IN = 123;
    public static final int REQUEST_LOCATION = 1000;
//    public static final int START_MAPS_ACTIVITY = 2000;
    // Firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference vaccinePointsDatabaseReference;
    private DatabaseReference claimsDatabaseReference;
    private ActivityFirebaseUiBinding binding;
    private LocationManager manager;
    private FirebaseUser user;
    public static final String VACCINE_POINTS = "vaccine_points";
    public static final String APPOINTMENTS = "appointments";
    public static final String CLAIMS = "claims";
    public static final String DOCTOR = "doctor";
    public static final String PATIENT = "patient";
    public static final String TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bind view for better performance
        binding = ActivityFirebaseUiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        vaccinePointsDatabaseReference = firebaseDatabase.getReference(VACCINE_POINTS);
        claimsDatabaseReference = firebaseDatabase.getReference(CLAIMS);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    binding.userName.setText(user.getDisplayName());
                    roleCheck(user);
                } else {
                    // User is signed out
                    createSignInIntent();
                }
            }
        };
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // prevent from re-creating sign in ui when rotating screen or leaving screen
        if (savedInstanceState == null) {
            createSignInIntent();
        }

    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_banner_virus)      // Set logo drawable
                        .setTheme(R.style.firebaseUIStyle)      // Set theme
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)   //saves users credentials to phone device
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                binding.userName.setText(user.getDisplayName());
                roleCheck(user);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise show
                Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    // create menu on the top left corner to sign out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sign_out) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }


    // save data to prevent losing them on screen rotation when app is running but not shown
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("f_user", user);
        super.onSaveInstanceState(outState);
    }

    // restore data that have been saved on onSaveInstanceState
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getParcelable("f_user");
        binding.userName.setText(user.getDisplayName());
        roleCheck(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    // method to sign out using AuthUI
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this);
    }


    // if user has granted permission for location go to MapsActivity
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
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


    /*
    Method used to check firebase realtime database about wich role has the current user. If the user
    has never signed in before he is added to the database as patient. If the user's uid is recorded in
    database as doctor he can check all the appointments for all the hospital. Patients can check for
    the nearest hospital and add appointmet
     */
    private void roleCheck(FirebaseUser firebaseUser) {
        // if the user is a doctor setup the ui
        DatabaseReference roleDoctor = claimsDatabaseReference.child(firebaseUser.getUid()).child(DOCTOR);
        roleDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    boolean isDoctor = dataSnapshot.getValue(boolean.class);
                    if(isDoctor){
                        binding.loginMessage.setText(getString(R.string.doctor_login_message));
                        binding.findHospital.setText(getString(R.string.shοw_hospital_list));
                        binding.findHospital.setOnClickListener(v -> showHospitals(v));
                    } else {
                        binding.loginMessage.setText(getString(R.string.user_login_message));
                        binding.findHospital.setText(getString(R.string.search_hospital));
                        binding.findHospital.setOnClickListener(v -> gps(v));
                    }
                }else {
                    binding.loginMessage.setText(getString(R.string.user_login_message));
                    binding.findHospital.setText(getString(R.string.search_hospital));
                    binding.findHospital.setOnClickListener(v -> gps(v));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        // if the user is a patient or if the user has never signed in before
        DatabaseReference rolePatient = claimsDatabaseReference.child(firebaseUser.getUid()).child(PATIENT);
        rolePatient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if the user has signed in before setup the ui for patients
                if(dataSnapshot.exists()){
                    boolean isPatient = dataSnapshot.getValue(boolean.class);
                    if(isPatient) {
                        binding.loginMessage.setText(getString(R.string.user_login_message));
                        binding.findHospital.setText(getString(R.string.search_hospital));
                        binding.findHospital.setOnClickListener(v -> gps(v));
                    }
                }else {
                    // if the user has never signed in before setup the ui for patients and add uid in db
                    claimsDatabaseReference.child(firebaseUser.getUid()).child(PATIENT).setValue(true);
                    claimsDatabaseReference.child(firebaseUser.getUid()).child(DOCTOR).setValue(false);
                    binding.loginMessage.setText(getString(R.string.user_login_message));
                    binding.findHospital.setText(getString(R.string.search_hospital));
                    binding.findHospital.setOnClickListener(v -> gps(v));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    /*
    Method used by patients to check if the gps is enabled, if access to location is permited and opens Maps activity
     */
    private void gps(View view) {
        // if gps is not enabled show message that asks to enable it
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDiabledDialog();
        } else {
            // if permission is not granted ask for it
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                // if permission is granted go to MapsActivity
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        }
    }

    // method for user that is doctor to go to HospitalListActivity
    private void showHospitals(View view) {
                startActivity(new Intent(getApplicationContext(), HospitalListActivity.class));
    }

    // if gps is not enabled shows dialog that informs user to enable it from phone settings
    public void showGPSDiabledDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.gps_title));
        alertDialog.setMessage(getString(R.string.gps_message));
        alertDialog.setPositiveButton(getString(R.string.gps_yes), new DialogInterface.OnClickListener() {
            // if user agrees to enable gps go to system settings
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(onGPS);
            }
        }).setNegativeButton(getString(R.string.gps_no), new DialogInterface.OnClickListener() {
            // if user selects no dialog disappears
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        alertDialog.create();
        alertDialog.show();
    }
}
