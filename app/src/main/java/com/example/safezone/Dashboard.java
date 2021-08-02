package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.safezone.fragments.DashboardFragment;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView drawerNav;
    private StorageReference storageReference;
    private TextView userName;
    private CircleImageView userIcon;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private PowerManager.WakeLock wl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "myapp:keepMeUp");

        drawerLayout = findViewById(R.id.drawer_menu);
        toolbar = findViewById(R.id.dashboard_toolbar);
        drawerNav = findViewById(R.id.dashboard_navMenu);

        View header = drawerNav.getHeaderView(0);
        userName = header.findViewById(R.id.drawer_nav_header_userName);
        userIcon = header.findViewById(R.id.drawer_nav_header_image);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference userImageRef = FirebaseDatabase.getInstance().getReference("userImage");
        user = FirebaseAuth.getInstance().getCurrentUser();

        // drawer menu nav logic

        drawerNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.drawer_nav_about:
                        Toast.makeText(Dashboard.this, "What about us ?", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_nav_profile:
                        Intent intent = new Intent(Dashboard.this,UserProfile.class);
                        startActivity(intent);
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

        // fetch user Icon
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModelClass userProfile = snapshot.getValue(UserModelClass.class);

                if (userProfile != null){

                    String phone = userProfile.getPhoneNumber();
                    userName.setText(phone);

                        userImageRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                UserModelClass temp = snapshot.getValue(UserModelClass.class);

                                if (temp != null){
                                    String img = temp.getUserIcon();
                                    Glide.with(Dashboard.this)
                                            .load(img)
                                            .fitCenter()
                                            .into(userIcon);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Ops, Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });

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
                                    startActivity(new Intent(Dashboard.this, HomeActivity.class));
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        wl.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wl.acquire(10*60*1000L /*10 minutes*/);
    }
}