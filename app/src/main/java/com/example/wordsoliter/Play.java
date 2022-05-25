package com.example.wordsoliter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Play#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Play extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Play() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Play.
     */
    // TODO: Rename and change types and number of parameters
    public static Play newInstance(String param1, String param2) {
        Play fragment = new Play();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play,
                container, false);
        Button easyButton =  view.findViewById(R.id.playbuttoneasy);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("mode", 1);
                startActivity(intent);
            }
        });
        Button normalButton =  view.findViewById(R.id.playbuttonnormal);
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("mode", 2);
                startActivity(intent);
            }
        });
        Button hardButton =  view.findViewById(R.id.playbuttonhard);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("mode", 3);
                startActivity(intent);
            }
        });
        Button insaneButton =  view.findViewById(R.id.playbuttoninsane);
        insaneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                intent.putExtra("mode", 4);
                startActivity(intent);
            }
        });
        return view;
    }

}