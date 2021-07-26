package com.example.safezone.fragments;

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

import com.example.safezone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetFragment extends Fragment {

    private TextView title;
    private EditText email;
    private ImageView icon;
    private Button button;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password_reset_fragment,container,false);

        title = view.findViewById(R.id.resetEmailTv);
        email = view.findViewById(R.id.resetEmail);
        icon = view.findViewById(R.id.resetEmailImage);
        button = view.findViewById(R.id.resetButton);
        progressBar = view.findViewById(R.id.reset_progressBar);
        auth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ressetPassword();
            }
        });


        return view;
    }

    private void ressetPassword () {

        String sEmail = email.getText().toString().trim();

        if (sEmail.isEmpty()){
            email.setError("Email is reuqired");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // send a link to user's email to reset his password
        auth.sendPasswordResetEmail(sEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "An email has been sent to reset your password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getActivity(), "Email is not registered", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}
