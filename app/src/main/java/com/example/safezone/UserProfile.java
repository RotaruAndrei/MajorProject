package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private TextView userMainViewName, userMainViewEmail, userViewName, userViewEmail, userViewPhone;
    private ImageView userMainViewImage ;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        initViews();

        // database reference for user image
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("userImage");

        // database reference to get current user details and display them
        databaseReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // save user details
                UserModelClass user = snapshot.getValue(UserModelClass.class);

                if (user != null){

                    userMainViewName.setText(user.getForename() + " " + user.getSurname());
                    userMainViewEmail.setText(user.getEmail());

                    userViewName.setText(user.getForename());
                    userViewEmail.setText(user.getEmail());
                    userViewPhone.setText(user.getPhoneNumber());


                    // create a new listener to get the image for the current user
                    // the image is in another node but the current user has the unique key
                    // than load the image

                        tempRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //save user details
                                UserModelClass justUser = snapshot.getValue(UserModelClass.class);

                                if (justUser != null){

                                    // get image link
                                    String img = justUser.getUserIcon();
                                    Glide.with(UserProfile.this)
                                            .load(img)
                                            .into(userMainViewImage);
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

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserDialog dialog = new UpdateUserDialog();
                dialog.show(getSupportFragmentManager(),"update user dialog");
            }
        });

    }

    // method that initate all the views
    private void initViews () {

        userMainViewName = findViewById(R.id.userProfile_userFullName);
        userMainViewEmail = findViewById(R.id.userProfile_userEmail);
        userMainViewImage = findViewById(R.id.userProfile_userMainImage);

        userViewName = findViewById(R.id.userProfile_userEditName);
        userViewEmail = findViewById(R.id.userProfile_userEditEmail);
        userViewPhone = findViewById(R.id.userProfile_userEditPhone);
        updateBtn = findViewById(R.id.userProfile_updateBtn);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}