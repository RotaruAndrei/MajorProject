package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView drawerNav;
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
                        new AlertDialog.Builder(Dashboard.this)
                                .setTitle("About Us")
                                .setMessage("Designed and Developed by Andrei Rotaru. \n" +
                                        "If interested about more developing projects please do visit")
                                .setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(Dashboard.this, AboutWebsite.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                                .create().show();
                        break;
                    case R.id.drawer_nav_profile:
                        Intent intent = new Intent(Dashboard.this,UserProfile.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_nav_terms:
                        new AlertDialog.Builder(Dashboard.this)
                                .setTitle("Terms and conditions")
                                .setMessage("For future development")
                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                        break;
                    case R.id.drawer_nav_licences:

                        new AlertDialog.Builder(Dashboard.this)
                                .setTitle("Licenses")
                                .setMessage(thirdPartyLicences())
                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();

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

    private String thirdPartyLicences () {
        String licences = "";
        licences += "Firebase\n\n";
        licences += "The MIT License (MIT)\n" +
                "\n" +
                "Copyright (c) 2014 Firebase\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                "in the Software without restriction, including without limitation the rights\n" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                "copies of the Software, and to permit persons to whom the Software is\n" +
                "furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all\n" +
                "copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                "SOFTWARE.\n\n";


        licences += "Glide\n\n";
        licences += "License for everything not in third_party and not otherwise marked:\n" +
                "\n" +
                "Copyright 2014 Google, Inc. All rights reserved.\n" +
                "\n" +
                "Redistribution and use in source and binary forms, with or without modification, are\n" +
                "permitted provided that the following conditions are met:\n" +
                "\n" +
                "   1. Redistributions of source code must retain the above copyright notice, this list of\n" +
                "         conditions and the following disclaimer.\n" +
                "\n" +
                "   2. Redistributions in binary form must reproduce the above copyright notice, this list\n" +
                "         of conditions and the following disclaimer in the documentation and/or other materials\n" +
                "         provided with the distribution.\n" +
                "\n" +
                "THIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED\n" +
                "WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n" +
                "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR\n" +
                "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
                "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
                "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n" +
                "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF\n" +
                "ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "\n" +
                "The views and conclusions contained in the software and documentation are those of the\n" +
                "authors and should not be interpreted as representing official policies, either expressed\n" +
                "or implied, of Google, Inc.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "License for third_party/disklrucache:\n" +
                "\n" +
                "Copyright 2012 Jake Wharton\n" +
                "Copyright 2011 The Android Open Source Project\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "License for third_party/gif_decoder:\n" +
                "\n" +
                "Copyright (c) 2013 Xcellent Creations, Inc.\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining\n" +
                "a copy of this software and associated documentation files (the\n" +
                "\"Software\"), to deal in the Software without restriction, including\n" +
                "without limitation the rights to use, copy, modify, merge, publish,\n" +
                "distribute, sublicense, and/or sell copies of the Software, and to\n" +
                "permit persons to whom the Software is furnished to do so, subject to\n" +
                "the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be\n" +
                "included in all copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
                "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n" +
                "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n" +
                "NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE\n" +
                "LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION\n" +
                "OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION\n" +
                "WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "License for third_party/gif_encoder/AnimatedGifEncoder.java and\n" +
                "third_party/gif_encoder/LZWEncoder.java:\n" +
                "\n" +
                "No copyright asserted on the source code of this class. May be used for any\n" +
                "purpose, however, refer to the Unisys LZW patent for restrictions on use of\n" +
                "the associated LZWEncoder class. Please forward any corrections to\n" +
                "kweiner@fmsware.com.\n" +
                "\n" +
                "-----------------------------------------------------------------------------\n" +
                "License for third_party/gif_encoder/NeuQuant.java\n" +
                "\n" +
                "Copyright (c) 1994 Anthony Dekker\n" +
                "\n" +
                "NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994. See\n" +
                "\"Kohonen neural networks for optimal colour quantization\" in \"Network:\n" +
                "Computation in Neural Systems\" Vol. 5 (1994) pp 351-367. for a discussion of\n" +
                "the algorithm.\n" +
                "\n" +
                "Any party obtaining a copy of these files from the author, directly or\n" +
                "indirectly, is granted, free of charge, a full and unrestricted irrevocable,\n" +
                "world-wide, paid up, royalty-free, nonexclusive right and license to deal in\n" +
                "this software and documentation files (the \"Software\"), including without\n" +
                "limitation the rights to use, copy, modify, merge, publish, distribute,\n" +
                "sublicense, and/or sell copies of the Software, and to permit persons who\n" +
                "receive copies from any such party to do so, with the only requirement being\n" +
                "that this copyright notice remain intact.\n\n";


        licences += "CircleImage\n\n";
        licences += "Copyright 2014 - 2020 Henning Dodenhof\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n\n";


        licences += "Dexter\n\n";
        licences += "Copyright 2015 Karumi\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n\n";

        return licences;
    }
}