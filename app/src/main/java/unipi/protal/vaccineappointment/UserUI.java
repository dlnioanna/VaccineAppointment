package unipi.protal.vaccineappointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import unipi.protal.vaccineappointment.databinding.UserUiBinding;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class UserUI extends AppCompatActivity {
    private UserUiBinding binding;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserUiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("currentUser");
        binding.userName.setText(user.getDisplayName());
        binding.findHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });

    }

}
