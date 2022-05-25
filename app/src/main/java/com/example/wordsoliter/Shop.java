package com.example.wordsoliter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shop extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  OpenDbHelper DbHelper;
    public SharedPreferences shPr;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shop.
     */
    // TODO: Rename and change types and number of parameters
    public static Shop newInstance(String param1, String param2) {
        Shop fragment = new Shop();
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
        View view = inflater.inflate(R.layout.fragment_shop,
                container, false);
        TextView money = view.findViewById(R.id.moneyView);
        shPr = getActivity().getSharedPreferences("com.example.wordsoliter", Context.MODE_PRIVATE);
        money.setText(Integer.toString(shPr.getInt("money", 1000)));
        Bitmap card1 = BitmapFactory.decodeResource(view.getResources(), R.drawable.cardback1);
        ImageButton cardb1 = view.findViewById(R.id.card_1);
        card1 = Bitmap.createScaledBitmap(card1, 150,  250, true);

        Bitmap card2 = BitmapFactory.decodeResource(view.getResources(), R.drawable.cardback2);
        ImageButton cardb2 = view.findViewById(R.id.card_2);
        card2 = Bitmap.createScaledBitmap(card2, 150,  250, true);

        Bitmap card3 = BitmapFactory.decodeResource(view.getResources(), R.drawable.cardback3);
        ImageButton cardb3 = view.findViewById(R.id.card_3);
        card3 = Bitmap.createScaledBitmap(card3, 160,  260, true);

        cardb1.setImageBitmap(card1);
        cardb2.setImageBitmap(card2);
        cardb3.setImageBitmap(card3);
        cardb1.setOnClickListener(this::shopListener);
        cardb2.setOnClickListener(this::shopListener);
        cardb3.setOnClickListener(this::shopListener);

        ImageButton backgrb1 = view.findViewById(R.id.background_1);
        Bitmap backgr1 = BitmapFactory.decodeResource(view.getResources(), R.drawable.background1);
        backgr1 = Bitmap.createScaledBitmap(backgr1, 150,  250, true);

        ImageButton backgrb2 = view.findViewById(R.id.background_2);
        Bitmap backgr2 = BitmapFactory.decodeResource(view.getResources(), R.drawable.background2);
        backgr2 = Bitmap.createScaledBitmap(backgr2, 150,  250, true);

        ImageButton backgrb3 = view.findViewById(R.id.background_3);
        Bitmap backgr3 = BitmapFactory.decodeResource(view.getResources(), R.drawable.background3);
        backgr3 = Bitmap.createScaledBitmap(backgr3, 150,  250, true);

        backgrb1.setImageBitmap(backgr1);
        backgrb2.setImageBitmap(backgr2);
        backgrb3.setImageBitmap(backgr3);
        backgrb1.setOnClickListener(this::shopListener);
        backgrb2.setOnClickListener(this::shopListener);
        backgrb3.setOnClickListener(this::shopListener);
        //DbHelper = new OpenDbHelper(view.getContext());
        //SQLiteDatabase db = DbHelper.getReadableDatabase();
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    public void shopListener(View v){
        boolean cardowned = false;
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shPr.edit();
        switch (v.getId()) {
            case R.id.card_1:
                if (cardowned){
                    editor.putInt("card", 1);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_card1));
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", 1000) - price);
                        editor.putInt("card", 1);
                    }
                }
                break;
            case R.id.card_2:
                if (cardowned){
                    editor.putInt("card", 2);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_card2));
                    Toast.makeText(getContext(), price + "", Toast.LENGTH_SHORT).show();
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", 1000) - price);
                        editor.putInt("card", 2);
                    }
                }
                break;
            case R.id.card_3:
                if (cardowned){
                    editor.putInt("card", 3);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_card3));
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", 1000) - price);
                        editor.putInt("card", 3);
                    }
                }
                break;
            case R.id.background_1:
                if (cardowned){
                    editor.putInt("background", 1);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_background1));
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", 1000) - price);
                        editor.putInt("background", 1);
                    }
                }
                break;
            case R.id.background_2:
                if (cardowned){
                    editor.putInt("background", 2);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_background2));
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", 1000) - price);
                        editor.putInt("background", 2);
                    }
                }
                break;
            case R.id.background_3:
                if (cardowned){
                    editor.putInt("background", 3);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_background1));
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", 1000) - price);
                        editor.putInt("background", 3);
                    }
                }
                break;
        }
        editor.apply();
        TextView money = Objects.requireNonNull(getActivity()).findViewById(R.id.moneyView);
        money.setText(shPr.getInt("money", 1000) + "");
    }
}