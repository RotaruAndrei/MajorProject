package com.example.safezone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.safezone.R;

public class RegisterFragment extends Fragment {

    private EditText forename, surname, email, password, confirmPassword;
    private TextView forenameTitle, surnameTitle, emailTitle, passwordTitle, confirmPasswordTittle;
    private ImageView forenameIcon, surnameIcon, emailIcon, passwordIcon, confirmPasswordIcon;
    private Button registerButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);


        forename = view.findViewById(R.id.registerFirstName);
        surname = view.findViewById(R.id.registerSurname);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.registerPassword);
        confirmPassword = view.findViewById(R.id.registerConfirmPassword);

        forenameTitle = view.findViewById(R.id.registerFirstNameTitle);
        surnameTitle = view.findViewById(R.id.registerSurnameTitle);
        emailTitle = view.findViewById(R.id.registerEmailTitle);
        passwordTitle = view.findViewById(R.id.registerPasswordTitle);
        confirmPasswordTittle = view.findViewById(R.id.registerConfirmPasswordTitle);

        forenameIcon = view.findViewById(R.id.registerFirstNameIcon);
        surnameIcon = view.findViewById(R.id.registerSurnameIcon);
        emailIcon = view.findViewById(R.id.registerEmailIcon);
        passwordIcon = view.findViewById(R.id.registerPasswordIcon);
        confirmPasswordIcon = view.findViewById(R.id.registerConfirmPasswordIcon);

        registerButton = view.findViewById(R.id.registerButton);

        return view;
    }
}
