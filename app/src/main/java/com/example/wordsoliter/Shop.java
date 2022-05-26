package com.example.wordsoliter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Shop extends Fragment {

    public SharedPreferences shPr;

    public Shop() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop,
                container, false);
        shPr = Objects.requireNonNull(getActivity()).getSharedPreferences("com.example.wordsoliter", Context.MODE_PRIVATE);
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
        update(view);
        return view;
    }

    @SuppressLint({"NonConstantResourceId", "MutatingSharedPrefs"})
    public void shopListener(View v){
        boolean cardowned = false;
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shPr.edit();
        Set<String> skinsOwned = new HashSet<>();
        skinsOwned.add("card_1");
        skinsOwned.add("background_1");
        skinsOwned = shPr.getStringSet("skins_owned", skinsOwned);
        switch (v.getId()) {
            case R.id.card_1:
                if (skinsOwned.contains("card_1")){
                    editor.putInt("cardchosen", 1);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_card1));
                    if (shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) >= price){
                        editor.putInt("money", shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) - price);
                        editor.putInt("cardchosen", 1);
                        skinsOwned.add("card_1");
                        editor.putStringSet("skins_owned", skinsOwned);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.card_2:
                if (skinsOwned.contains("card_2")){
                    editor.putInt("cardchosen", 2);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_card2));
                    if (shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) >= price){
                        editor.putInt("money", shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) - price);
                        editor.putInt("cardchosen", 2);
                        skinsOwned.add("card_2");
                        editor.putStringSet("skins_owned", skinsOwned);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.card_3:
                if (skinsOwned.contains("card_3")){
                    editor.putInt("cardchosen", 3);

                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_card3));
                    if (shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) >= price){
                        editor.putInt("money", shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) - price);
                        editor.putInt("cardchosen", 3);
                        skinsOwned.add("card_3");
                        editor.putStringSet("skins_owned", skinsOwned);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.background_1:
                if (skinsOwned.contains("background_1")){
                    editor.putInt("backgroundchosen", 1);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_background1));
                    if (shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) >= price){
                        editor.putInt("money", shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) - price);
                        editor.putInt("backgroundchosen", 1);
                        skinsOwned.add("background_1");
                        editor.putStringSet("skins_owned", skinsOwned);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.background_2:
                if (skinsOwned.contains("background_2")){
                    editor.putInt("backgroundchosen", 2);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_background3));
                    if (shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) >= price){
                        editor.putInt("money", shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) - price);
                        editor.putInt("backgroundchosen", 2);
                        skinsOwned.add("background_2");
                        editor.putStringSet("skins_owned", skinsOwned);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.background_3:
                if (skinsOwned.contains("background_3")){
                    editor.putInt("backgroundchosen", 3);
                }
                else{
                    int price = Integer.parseInt(getResources().getString(R.string.price_background1));
                    if (shPr.getInt("money", 1000) >= price){
                        editor.putInt("money", shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) - price);
                        editor.putInt("backgroundchosen", 3);
                        skinsOwned.add("background_3");
                        editor.putStringSet("skins_owned", skinsOwned);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        editor.apply();
        update(Objects.requireNonNull(getView()));
    }

    @SuppressLint("SetTextI18n")
    public void update(View v){
        TextView card1price = v.findViewById(R.id.card1price),
                card2price = v.findViewById(R.id.card2price),
                card3price = v.findViewById(R.id.card3price),
                background1price = v.findViewById(R.id.background1price),
                background2price = v.findViewById(R.id.background2price),
                background3price = v.findViewById(R.id.background3price);
        Set<String> skinsOwned = new HashSet<>();
        skinsOwned.add("card_1");
        skinsOwned.add("background_1");
        skinsOwned = shPr.getStringSet("skins_owned", skinsOwned);
        for(String skin: skinsOwned)
            switch (skin){
                case "card_1":
                    card1price.setText("Имеется");
                    break;
                case "card_2":
                    card2price.setText("Имеется");
                    break;
                case "card_3":
                    card3price.setText("Имеется");
                    break;
                case "background_1":
                    background1price.setText("Имеется");
                    break;
                case "background_2":
                    background2price.setText("Имеется");
                    break;
                case "background_3":
                    background3price.setText("Имеется");
                    break;
            }
        switch (shPr.getInt("cardchosen", 1)){
            case 1:
                card1price.setText("Выбрано");
                break;
            case 2:
                card2price.setText("Выбрано");
                break;
            case 3:
                card3price.setText("Выбрано");
                break;
        }
        switch (shPr.getInt("backgroundchosen", 1)){
            case 1:
                background1price.setText("Выбрано");
                break;
            case 2:
                background2price.setText("Выбрано");
                break;
            case 3:
                background3price.setText("Выбрано");
                break;
        }
        TextView money = v.findViewById(R.id.moneyView);
        money.setText(shPr.getInt("money", getResources().getInteger(R.integer.moneydefualt)) + "");
    }
}