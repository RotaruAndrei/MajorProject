package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.safezone.fragments.LoginFragment;
import com.example.safezone.fragments.RegisterFragment;
import com.example.safezone.fragments.ResetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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