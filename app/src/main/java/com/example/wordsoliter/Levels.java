package com.example.wordsoliter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.SplittableRandom;

public class Levels {
    private ArrayList<ArrayList<String>> levels = new ArrayList<>();
    private ArrayList<Integer> countColumn = new ArrayList<Integer>();
    public Levels() {
        ArrayList<String> a = new ArrayList<String>();
        {
            a.add("собака");
            a.add("сумка");
            a.add("енот");
            a.add("лимон");
        }
        levels.add(a);
        countColumn.add(6);
    }

    public int getCountColumn(int i) {
        return countColumn.get(i);
    }
    public ArrayList<String> getLevel(int i){
        return levels.get(i);
    }
}
