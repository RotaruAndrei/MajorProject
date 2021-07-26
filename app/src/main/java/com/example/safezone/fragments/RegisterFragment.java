package com.example.safezone.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safezone.HomeActivity;
import com.example.safezone.R;
import com.example.safezone.UserModelClass;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class RegisterFragment extends Fragment {

    private EditText forename, surname, email, password, confirmPassword, phoneEdit;
    private TextView forenameTitle, surnameTitle, emailTitle, passwordTitle, confirmPasswordTittle,phoneTitle;
    private ImageView forenameIcon, surnameIcon, emailIcon, passwordIcon, confirmPasswordIcon,phoneIcon;
    private Button registerButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);

        phoneEdit = view.findViewById(R.id.registerPhoneEdit);
        forename = view.findViewById(R.id.registerFirstName);
        surname = view.findViewById(R.id.registerSurname);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.registerPassword);
        confirmPassword = view.findViewById(R.id.registerConfirmPassword);

        phoneTitle = view.findViewById(R.id.registerPhoneTitle);
        forenameTitle = view.findViewById(R.id.registerFirstNameTitle);
        surnameTitle = view.findViewById(R.id.registerSurnameTitle);
        emailTitle = view.findViewById(R.id.registerEmailTitle);
        passwordTitle = view.findViewById(R.id.registerPasswordTitle);
        confirmPasswordTittle = view.findViewById(R.id.registerConfirmPasswordTitle);

        phoneIcon = view.findViewById(R.id.registerPhoneIcon);
        forenameIcon = view.findViewById(R.id.registerFirstNameIcon);
        surnameIcon = view.findViewById(R.id.registerSurnameIcon);
        emailIcon = view.findViewById(R.id.registerEmailIcon);
        passwordIcon = view.findViewById(R.id.registerPasswordIcon);
        confirmPasswordIcon = view.findViewById(R.id.registerConfirmPasswordIcon);

        progressBar = view.findViewById(R.id.register_ProgressBar);

        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        return view;
    }

    // logic for user registration

    private void registerUser() {
        mAuth = FirebaseAuth.getInstance();

        String sForname = forename.getText().toString().trim();
        String sSurname = surname.getText().toString().trim();
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();
        String sConfirmPassword = confirmPassword.getText().toString().trim();
        String sPhoneNumber = phoneEdit.getText().toString().trim();


        // empty field for phone numner
        if (sPhoneNumber.isEmpty()){
            phoneEdit.setError("Phone Number is required");
            phoneEdit.requestFocus();
            return;
        }

        // no empty fields

        if (sForname.isEmpty()){
            forename.setError("Forename is required");
            forename.requestFocus();
            return;
        }

        if (sSurname.isEmpty()){
            surname.setError("Surname is required");
            surname.requestFocus();
            return;
        }

        if (sEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        // a valid pattern for email verification

        if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }

        if (sPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (sPassword.length() < 6){
            password.setError("Password is less than 6 characters, please provide another password");
            password.requestFocus();
            return;
        }


        if (sConfirmPassword.isEmpty()){
            confirmPassword.setError("Confirm your password is required");
            confirmPassword.requestFocus();
            return;
        }

        if (!sConfirmPassword.equals(sPassword)){

            confirmPassword.setError("Passwords does not matches");
            confirmPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        // firebase logic
        // create authentification with email and password
        // and then save the user to fire base
        mAuth.createUserWithEmailAndPassword(sEmail,sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            UserModelClass user = new UserModelClass(sForname,sSurname,sEmail,sPhoneNumber);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "The user has been registered succesufully", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(getContext(), "User registration failed", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }else {
                            Toast.makeText(getContext(), "Registration failed, try again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }



}
