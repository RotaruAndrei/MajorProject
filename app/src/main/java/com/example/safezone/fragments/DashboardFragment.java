package com.example.safezone.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safezone.R;
import com.example.safezone.UploadFile;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment {

    private MaterialCardView sendCard, complainCard, feedbackCard;
    private ExtendedFloatingActionButton sendBtn, cancelBtn;
    private LocationRequest locationRequest;

    // call back location
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLocations().size() > 0){

                int latestLocationIndex = locationResult.getLocations().size() - 1;

                double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                Log.d(TAG, "onLocationResult: " + " Lat: " + latitude + ", Long: " + longitude);

            }
        }
    };

    //suppress floating button on touch listener
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment,container,false);

        sendCard = view.findViewById(R.id.dashboard_sendCard);
        complainCard = view.findViewById(R.id.dashboard_your_rightsCard);
        feedbackCard = view.findViewById(R.id.dashboard_newsCard);
        sendBtn = view.findViewById(R.id.dashboard_extendedFloatingButton);
        cancelBtn = view.findViewById(R.id.dashboard_extendedFloatingButton_CanceSOS);

        locationRequest = LocationRequest.create();

        sendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadFile.class);
                startActivity(intent);
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for permission when pressing send SOS
                // and set visibility gone for send button if the feature is running
                    checkForPermission();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the user stops the SOS feature set visibility gone to cancel button

                new AlertDialog.Builder(getContext())
                        .setTitle("Close S.O.S service")
                        .setMessage("Are you safe?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LocationServices.getFusedLocationProviderClient(getContext())
                                        .removeLocationUpdates(locationCallback);
                                Log.d(TAG, "onClick: " + "GPS location ended");
                                cancelBtn.setVisibility(View.GONE);
                                sendBtn.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });

        return view;
    }

    // check for user permission
    // and suppress warning cuz the permission check from the user is made through Dexter
    @SuppressWarnings("MissingPermission")
    private void checkForPermission () {

        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        // if permission is granted open a dialog to ask the user if he really
                        // wants to send SOS signal

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Start S.O.S signal")
                                .setMessage("Are you in need for help?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        // create location request
                                        locationRequest.setInterval(4000);
                                        locationRequest.setFastestInterval(2000);
                                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                                        // request location updates
                                        LocationServices.getFusedLocationProviderClient(getActivity())
                                                .requestLocationUpdates(locationRequest, locationCallback,Looper.getMainLooper());
                                        sendBtn.setVisibility(View.GONE);
                                        cancelBtn.setVisibility(View.VISIBLE);


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }

}
