package com.example.wordsoliter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Play extends Fragment {

    public Play() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play,
                container, false);
        Button easyButton =  view.findViewById(R.id.playbuttoneasy);
        easyButton.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("mode", 1);
            startActivity(intent);
        });
        Button normalButton =  view.findViewById(R.id.playbuttonnormal);
        normalButton.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("mode", 2);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        Button hardButton =  view.findViewById(R.id.playbuttonhard);
        hardButton.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("mode", 3);
            startActivity(intent);
        });
        Button insaneButton =  view.findViewById(R.id.playbuttoninsane);
        insaneButton.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            intent.putExtra("mode", 4);
            startActivity(intent);
        });
        return view;
    }

}