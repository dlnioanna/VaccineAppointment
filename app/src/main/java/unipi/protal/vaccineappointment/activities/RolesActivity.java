package unipi.protal.vaccineappointment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import unipi.protal.vaccineappointment.R;

public class RolesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles);

//        UserRecord userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(phoneNumber);
//// See the UserRecord reference doc for the contents of userRecord.
//        System.out.println("Successfully fetched user data: " + userRecord.getPhoneNumber());
    }
}