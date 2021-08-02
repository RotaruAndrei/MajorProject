package com.example.safezone.fragments;


import android.content.Intent;
import android.os.Bundle;
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
import com.example.safezone.Dashboard;
import com.example.safezone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    public static final String REMEMBER_USER = "rememberUser";
    private EditText email, password;
    private TextView emailTitle, passwordTitle, remember;
    private ImageView emailIcon, passwordIcon;
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
        emailIcon = view.findViewById(R.id.loginEmailImage);
        passwordIcon = view.findViewById(R.id.loginPasswordImage);
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

                    // get current user id

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // a check if the user veryfied his email in order to login in

                    if (user.isEmailVerified()){

                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {

                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(getActivity(), "Check your email in order to verify your account", Toast.LENGTH_SHORT).show();
                    }




                }else {
                    Toast.makeText(getActivity(), "Login failed, email or password incorrect, try again", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
