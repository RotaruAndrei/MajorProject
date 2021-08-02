package com.example.safezone.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.safezone.LocationServiceWorker;
import com.example.safezone.NewsBoard;
import com.example.safezone.NewsItem;
import com.example.safezone.R;
import com.example.safezone.UploadFile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment {

    private MaterialCardView sendCard,newsCard;

    private ExtendedFloatingActionButton sendBtn, cancelBtn;
    private FirebaseUser currentUser;
    //create a variable to check if the Emergency button has been activated
    public static boolean isActivated;
//     create local variable for worker Manager
    private WorkManager workManager;
    //create a local variable for worker request id
    private UUID locID;
    //key and value to stop the task

    //suppress floating button on touch listener
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {





        View view = inflater.inflate(R.layout.dashboard_fragment,container,false);
        sendCard = view.findViewById(R.id.dashboard_sendCard);
        sendBtn = view.findViewById(R.id.dashboard_extendedFloatingButton);
        cancelBtn = view.findViewById(R.id.dashboard_extendedFloatingButton_CanceSOS);
        newsCard = view.findViewById(R.id.dashboard_newsCard);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        workManager = WorkManager.getInstance(getActivity());

        sendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadFile.class);
                startActivity(intent);
            }
        });

        newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsBoard.class);
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

                //the SOS button has been deactivated
                isActivated = false;

                // if the user stops the SOS feature set visibility gone to cancel button
                //  show the alert dialog if the user wants to cancel
                new AlertDialog.Builder(getContext())
                        .setTitle("Close S.O.S service")
                        .setMessage("Are you safe?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                workManager.cancelWorkById(locID);
                                workManager.cancelAllWorkByTag("Location");

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

        // check for permision using Dexter library
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

                                        // SOS button is active
                                        isActivated = true;

                                        //create a simpler request
                                        OneTimeWorkRequest locRequest = new OneTimeWorkRequest.Builder(LocationServiceWorker.class)
                                                .addTag("Location")
                                                .build();

                                        locID = locRequest.getId();
                                        //create the enqueue for the request
                                        workManager.enqueueUniqueWork("userLocation",ExistingWorkPolicy.REPLACE,locRequest);
//                                        // set buttons visibility accordingly
                                        sendBtn.setVisibility(View.GONE);

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
                        Toast.makeText(getActivity(), "Permission was denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();


    }


    @Override
    public void onResume() {
        super.onResume();

        if (isActivated){
            sendBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
        }else {
            cancelBtn.setVisibility(View.GONE);
            sendBtn.setVisibility(View.VISIBLE);
        }
    }

}
