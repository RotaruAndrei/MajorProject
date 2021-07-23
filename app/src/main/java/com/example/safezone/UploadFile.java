package com.example.safezone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.safezone.fragments.DashboardFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class UploadFile extends AppCompatActivity {
    private static final int PICK_VIDEO = 1;

    // declare UI and firebase

    private VideoView videoView;
    private Button uploadBtn, selectVideoBtn;
    private EditText videoTitle;
    private ProgressBar progressBar;
    private Uri videoUri;
    private MediaController mediaController;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private UploadTask uploadTask;
    private UserVideosModelClass userVideosModelClass;
    private FirebaseUser currentUser;
    private ActivityResultLauncher<String> getMediaContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        userVideosModelClass = new UserVideosModelClass();
        storageReference = FirebaseStorage.getInstance().getReference("Videos");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uservideos");

        videoView = findViewById(R.id.upload_VideoView);
        uploadBtn = findViewById(R.id.upload_UploadButton);
        selectVideoBtn = findViewById(R.id.upload_ChoseButton);
        videoTitle = findViewById(R.id.upload_VideoTitle);
        progressBar = findViewById(R.id.upload_ProgressBar);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();


        getMediaContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                if (result != null){
                    videoUri = result;
                    videoView.setVideoURI(result);
                }
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideoFile();
            }
        });

        selectVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            checkForPermission();
            }
        });

    }

    // check for user permission using Dexter library
    private void checkForPermission () {

    Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    getMediaContent.launch("video/*");
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Toast.makeText(UploadFile.this, "Permission was denied", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();

}


    // create a method to detect the extension of the file for pc purpouse
    private String getFileExtension (Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // create a method to upload the video file
    private void UploadVideoFile () {

        String videoName = videoTitle.getText().toString().trim();

        if (videoUri != null || !TextUtils.isEmpty(videoName)){
            progressBar.setVisibility(View.VISIBLE);
            // detect file extension
            final StorageReference storageRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(videoUri));
            uploadTask = storageRef.putFile(videoUri);


            // create a task to retrieve video from firebase
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){

                        throw task.getException();
                    }

                    return storageRef.getDownloadUrl();
                }
            })
                    // save video file - data in the database
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()){

                                progressBar.setVisibility(View.GONE);
                                Uri downloadFile = task.getResult();

                                Toast.makeText(UploadFile.this, "File saved successfully", Toast.LENGTH_SHORT).show();

                                //  save the current user into a variable
                                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                if (currentUser != null){

                                    DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("Users");
                                    String userID = currentUser.getUid();
                                    tempRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            UserModelClass tempUser = snapshot.getValue(UserModelClass.class);

                                            if (tempUser != null){

                                                String currentUserName = tempUser.getForname() + " " + tempUser.getSurname();

                                                userVideosModelClass.setUserName(currentUserName);
                                                userVideosModelClass.setVideoTitle(videoName);
                                                userVideosModelClass.setVideoUrl(downloadFile.toString());

                                                // push data to firebase

                                                String ref = databaseReference.push().getKey();
                                                databaseReference.child(ref).setValue(userVideosModelClass);

                                                //send user back to the dashboard
                                                Intent intent = new Intent(UploadFile.this, Dashboard.class);
                                                startActivity(intent);

                                            }else {
                                                Toast.makeText(UploadFile.this, "Unable to load user values", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }else {
                                    Toast.makeText(UploadFile.this, "Unable to load current user", Toast.LENGTH_SHORT).show();
                                }


                            }else {
                                Toast.makeText(UploadFile.this, "Task was not successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



    // override the back press method to redirect the user back to dashboard
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}