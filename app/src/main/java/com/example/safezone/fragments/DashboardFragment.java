package com.example.safezone.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.safezone.R;
import com.example.safezone.UploadFile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class DashboardFragment extends Fragment {

    private MaterialCardView sendCard, complainCard, feedbackCard;
    private ExtendedFloatingActionButton button;
    private FragmentActivity frg;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            frg = (FragmentActivity)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment,container,false);

        sendCard = view.findViewById(R.id.dashboard_sendCard);
        complainCard = view.findViewById(R.id.dashboard_your_rightsCard);
        feedbackCard = view.findViewById(R.id.dashboard_newsCard);
        button = view.findViewById(R.id.dashboard_extendedFloatingButton);

        sendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadFile.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
