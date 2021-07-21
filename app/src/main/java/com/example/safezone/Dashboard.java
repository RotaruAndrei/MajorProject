package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safezone.fragments.DashboardFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView drawerNav;

    private TextView userName;
    private ImageView userIcon;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_menu);
        toolbar = findViewById(R.id.dashboard_toolbar);
        drawerNav = findViewById(R.id.dashboard_navMenu);

        userIcon = findViewById(R.id.drawer_nav_header_image);




        // drawer menu nav logic

        drawerNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.drawer_nav_about:
                        Toast.makeText(Dashboard.this, "What about us ?", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });



        // set toolbar
        setSupportActionBar(toolbar);

        //create a toggle for drawer menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container,new DashboardFragment()).commit();

        userDetails();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        return true;
    }

    // toolbar items, logout
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.dashboard_toolbar_logout:

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("Logout? Are you sure?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(Dashboard.this,MainActivity.class));
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();

                break;

        }
        return true;
    }

    // method to fill drawer menu with user details
    private void userDetails(){

        user = FirebaseAuth.getInstance().getCurrentUser();

        if  (user != null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            if (databaseReference != null){
                userID = user.getUid();
                if (userID != null){

                    databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            UserModelClass userProfile = snapshot.getValue(UserModelClass.class);

                            if (userProfile != null){

                                String firstName = userProfile.getForname();
                                String secondName = userProfile.getSurname();

                                View header = drawerNav.getHeaderView(0);
                                userName = header.findViewById(R.id.drawer_nav_header_userName);
                                userName.setText(firstName + " "+  secondName);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Dashboard.this, "Ops, Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }


    }



}