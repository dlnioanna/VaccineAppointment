package unipi.protal.vaccineappointment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import unipi.protal.vaccineappointment.databinding.ActivityFirebaseUiBinding;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FirebaseUIActivity extends AppCompatActivity implements LocationListener {
    private static final int RC_SIGN_IN = 123;
    private static final int REQUEST_LOCATION= 1000;
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "MainActivity";
    // Firebase instance variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String mUsername;
    private ActivityFirebaseUiBinding binding;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirebaseUiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mUsername = ANONYMOUS;
        // Initialize Firebase components
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference().child("vaccine_point");
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    createSignInIntent();
                }
            }
        };
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        binding.signOutButton.setOnClickListener(v->signOut());
        binding.findHospital.setOnClickListener(v->gps(v));
        createSignInIntent();
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
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
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Intent intent = new Intent(this,UserUI.class);
//                intent.putExtra("currentUser",user);
//                startActivity(intent);
                binding.userName.setText(user.getDisplayName());
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise show
                // response.getError().getMessage().
                Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    // [END auth_fui_result]



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
        // todo clear view
        detachDatabaseReadListener();
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //todo otan prostheto astheni h allh efarmogh na ananeonetai
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        onSignedOutCleanup();
                    }
                });
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        // todo clear view
        detachDatabaseReadListener();

    }

    private void detachDatabaseReadListener() {
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    private void gps(View view){
        if(ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
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
