package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.safezone.fragments.LoginFragment;
import com.example.safezone.fragments.RegisterFragment;
import com.example.safezone.fragments.ResetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.safezone.fragments.LoginFragment.REMEMBER_USER;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth authentication;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.mainBottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new LoginFragment()).commit();
        authentication = FirebaseAuth.getInstance();
        user = authentication.getCurrentUser();


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

    @Override
    protected void onStart() {
        super.onStart();

        if (user != null){

            Intent intent = new Intent(this,Dashboard.class);
            startActivity(intent);
        }
    }
}