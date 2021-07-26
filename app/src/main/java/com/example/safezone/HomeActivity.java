package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyScanManager;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safezone.fragments.LoginFragment;
import com.example.safezone.fragments.RegisterFragment;
import com.example.safezone.fragments.ResetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.mainBottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new LoginFragment()).commit();




    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()){
                case R.id.bottomNavLogin:
                    fragment = new LoginFragment();
                    break;

                case R.id.bottomNavReset:
                    fragment = new ResetFragment();
                    break;

                case R.id.bottomNavRegister:
                    fragment = new RegisterFragment();
                    break;
                default:
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
            return true;
        }
    };

}