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


public class ResetFragment extends Fragment {

    private TextView title;
    private EditText email;
    private ImageView icon;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password_reset_fragment,container,false);

        title = view.findViewById(R.id.resetEmailTv);
        email = view.findViewById(R.id.resetEmail);
        icon = view.findViewById(R.id.resetEmailImage);
        button = view.findViewById(R.id.resetButton);


        return view;
    }
}
