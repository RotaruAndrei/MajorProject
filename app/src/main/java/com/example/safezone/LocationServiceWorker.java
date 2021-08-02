package com.example.safezone;


import android.annotation.SuppressLint;


import android.content.Context;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LocationServiceWorker extends Worker {
    private static final String TAG = "LocationServiceWorker";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FirebaseUser currentUser;
    private String emergencyUserkey;
    private DatabaseReference databaseReference;
    UserEmergencyModelClass user = new UserEmergencyModelClass();

    public LocationServiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Result doWork() {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Emergency");


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationRequest = LocationRequest.create();
        // create a location request
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);




        //create a new emergency user model and set its value


        // call back interface to update the location
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                if (locationResult != null) {

                    int indexNumber = locationResult.getLocations().size() - 1;

                    double latitude = locationResult.getLocations().get(indexNumber).getLatitude();
                    double longitude = locationResult.getLocations().get(indexNumber).getLongitude();

                    // create an hash map to update the user location
                    HashMap hashMap = new HashMap();
                    hashMap.put("userLatitude", latitude);
                    hashMap.put("userLongitude", longitude);

                    // update current user location
                    databaseReference.child(emergencyUserkey).updateChildren(hashMap);

                    Log.d(TAG, "onLocationResult: " + "Lat: " + latitude + ", Long: " + longitude);

                } else {

                    Toast.makeText(getApplicationContext(), "Update service has been stoped", Toast.LENGTH_SHORT).show();
                }
            }
        };


        // get the current user values to update the values for emergency user
        // send user location to firebase
        if (currentUser != null) {


            DatabaseReference tempUser = FirebaseDatabase.getInstance().getReference("Users");
            tempUser.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    UserModelClass tempUser = snapshot.getValue(UserModelClass.class);

                    if (tempUser != null) {

                        //get current user name and phone number
                        String tempName = tempUser.getForename() + " " + tempUser.getSurname();
                        String tempPhone = tempUser.getPhoneNumber();

                        user.setUserName(tempName);
                        user.setUserPhoneNumber(tempPhone);
                        user.setUserLatitude(0);
                        user.setUserLongitude(0);

                        // get user key then send it to firebase
                        emergencyUserkey = databaseReference.push().getKey();
                        databaseReference.child(emergencyUserkey).setValue(user);


                    } else {
                        Toast.makeText(getApplicationContext(), "User value is null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper());

        return Result.success();
    }

    @Override
    public void onStopped() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    
}




