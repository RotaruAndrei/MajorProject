package com.example.safezone;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class  UpdateUserDialog extends DialogFragment {

    private static final String TAG = "dialog speaking";
    private Button dismissBtn, submitBtn, uploadImage;
    private EditText editForname, editSurname, editEmail, editPhone;
    private CircleImageView img;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private Uri imageUri;
    private String imageURLString;
    private HashMap updateHashMap;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.update_dialog, null);
        initViews(view);
        updateHashMap = new HashMap();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("userImage");
        storage = FirebaseStorage.getInstance().getReference("UserIMG");

        //create a register result
        // new contract to media file
        ActivityResultLauncher<String> getMedia = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    imageUri = result;
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(imageUri)
                            .into(img);
                }
            }
        });


        // create the alert dialog
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity())
                .setView(view);

        // dismiss the dialog
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //submit all the new information to firebase
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            uploadImage();
            }
        });

        // get media if permission granted
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ask for user permission
                // get media if permission is granted
                // if denied ask again
                Dexter.withContext(getActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                getMedia.launch("image/*");

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
        });

        return builder.create();
    }

    //set views
    private void initViews(View view) {

        dismissBtn = view.findViewById(R.id.customDialog_dismissBtn);
        submitBtn = view.findViewById(R.id.customDialog_submitBtn);
        uploadImage = view.findViewById(R.id.customDialog_uploadBtn);
        editForname = view.findViewById(R.id.customDialog_InformationLayout_editForename);
        editSurname = view.findViewById(R.id.customDialog_InformationLayout_editSurname);
        editEmail = view.findViewById(R.id.customDialog_InformationLayout_editEmail);
        editPhone = view.findViewById(R.id.customDialog_InformationLayout_editPhone);
        img = view.findViewById(R.id.customDialog_Image);

    }

    // method to upload image
    // if the user wants to update the profile picture
    private void uploadImage() {

        //get current user reference and values
        DatabaseReference current = FirebaseDatabase.getInstance().getReference("Users");
        current.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModelClass userModelClass = snapshot.getValue(UserModelClass.class);

                if (userModelClass != null){

                    StorageReference tempRef = storage.child(currentUser.getUid());

                    // in case the user dont select any picture
                    if (imageUri != null){

                        tempRef.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        tempRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // save download url
                                                imageURLString = uri.toString();
                                                // create new java object with downloaded url
                                                UserModelClass user = new UserModelClass(imageURLString);
                                                // save new created user to firebase
                                                databaseReference.child(currentUser.getUid()).setValue(user);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Upload failed!, try again", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                        // get the progess in double
                                        double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();


                                    }
                                }) ;
                    }

                    // if the user dont select any image update user fields
                    updateUserDetails();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast.makeText(getActivity(), "User successfully updated", Toast.LENGTH_SHORT).show();
        // send user back to dash board activity after successfully upload the updates
        Intent intent = new Intent(getActivity(),Dashboard.class);
        startActivity(intent);
        dismiss();
    }

    // methods that updates user details
    private void updateUserDetails (){


        String _firstName = editForname.getText().toString();
        String _lastName = editSurname.getText().toString();
        String _email = editEmail.getText().toString();
        String _phone = editPhone.getText().toString();

        //lets see

        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference userImage = FirebaseDatabase.getInstance().getReference("userImage");
        currentUserRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModelClass tempUser = snapshot.getValue(UserModelClass.class);


                String firstName = tempUser.getForename();
                String lastName = tempUser.getSurname();
                String email = tempUser.getEmail();
                String phone = tempUser.getPhoneNumber();


                if (!_firstName.isEmpty()){
                    updateHashMap.put("forename",_firstName);
                }else {
                    updateHashMap.put("forename",firstName);
                }

                if (!_lastName.isEmpty()){
                    updateHashMap.put("surname",_lastName);
                }else {
                    updateHashMap.put("surname",lastName);
                }

                if (!_email.isEmpty()){
                    updateHashMap.put("email",_email);
                }else {
                    updateHashMap.put("email",email);
                }

                if (!_phone.isEmpty()){
                    updateHashMap.put("phoneNumber",_phone);
                }else {
                    updateHashMap.put("phoneNumber",phone);
                }

                currentUserRef.child(currentUser.getUid()).updateChildren(updateHashMap);

//                temp.child(currentUser.getUid()).updateChildren(updateHashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
