package com.example.safezone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safezone.Dashboard;
import com.example.safezone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private EditText email, password;
    private TextView emailTitle, passwordTitle, remember;
    private ImageView emailIcon, passwordIcon;
    private CheckBox checkBox;
    private Button button;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);

        email = view.findViewById(R.id.loginEmail);
        password = view.findViewById(R.id.loginPassword);
        emailTitle = view.findViewById(R.id.loginEmailTv);
        passwordTitle = view.findViewById(R.id.loginPasswordTv);
        remember = view.findViewById(R.id.loginCheckboxTitle);
        emailIcon = view.findViewById(R.id.loginEmailImage);
        passwordIcon = view.findViewById(R.id.loginPasswordImage);
        checkBox = view.findViewById(R.id.loginCheckBox);
        button = view.findViewById(R.id.loginButton);
        progressBar = view.findViewById(R.id.login_Fragment_ProgressBar);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });

        return view;
    }

    // firebase logic
    
    private void userLogin (){

        mAuth = FirebaseAuth.getInstance();
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        if (sEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (sPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // login firebase logic

        mAuth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else {
                    Toast.makeText(getActivity(), "Login failed, email or password incorrect, try again", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
