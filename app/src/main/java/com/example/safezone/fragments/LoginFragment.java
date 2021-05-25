package com.example.safezone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safezone.Dashboard;
import com.example.safezone.R;

public class LoginFragment extends Fragment {

    private EditText email, password;
    private TextView emailTitle, passwordTitle, remember;
    private ImageView emailIcon, passwordIcon;
    private CheckBox checkBox;
    private Button button;


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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
