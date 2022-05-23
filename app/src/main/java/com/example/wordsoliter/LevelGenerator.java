package com.example.wordsoliter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class LevelGenerator {
    private final ArrayList<ArrayList<String>> level = new ArrayList<>();
    private final ArrayList<String> dictWords = new ArrayList<>();
    private final ArrayList<Float> dictRate = new ArrayList<>();

    public LevelGenerator(ArrayList<String> dictionary) {
        for (String line : dictionary) {
            dictWords.add(line.split(" ")[0]);
            dictRate.add(Float.valueOf(line.split(" ")[1]));
        }
    }

    public Level generateLevel(int tier) {
        int l = 0;
        int r = 6979;
        int countWords = 6;
        int minLenWord = 4;
        int maxLenWord = 6;
        switch (tier) {
            case (1):
                l = 0;
                r = 2000;
                break;
            case (2):
                l = 0;
                r = 10377;
                break;
            case (3):
                l = 0;
                r = 14421;
                break;
            case (4):
                l = 0;
                r = 93421;
                break;
        }
        ArrayList<String> dictForLevel = new ArrayList<>(dictWords.subList(l, r));
        dictForLevel = dictForLevel.stream().filter(x -> x.length() <= maxLenWord && x.length() >= minLenWord).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(dictForLevel);
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < countWords; i++) {
            words.add(dictForLevel.get(i));
        }
        return new Level(words);
    }

    public String get(int ind) {
        return dictWords.get(ind);
    }

    class Level {
        ArrayList<ArrayList<Integer>> answers_ind;
        ArrayList<ArrayList<String>> level;
        ArrayList<String> answers_words;

        Level(ArrayList<String> words) {
            int CountColumn = words.get(0).length();
            int index = 0;

            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).length() > CountColumn) {
                    CountColumn = words.get(i).length();
                    index = i;
                }
            }
            level = new ArrayList<>();
            answers_ind = new ArrayList<>();
            answers_words = new ArrayList<>();
            for (int i = 0; i < CountColumn; i++)
                level.add(new ArrayList<String>());
            for (String word : words) {
                ArrayList<Integer> ans_ind = new ArrayList<>();
                ArrayList<Integer> possibleColumn = new ArrayList<>();
                for (int i = 0; i < CountColumn; i++) possibleColumn.add(i);
                for (int j = 0; j < word.length(); j++) {
                    int a = getRandomInt(possibleColumn.size());
                    ans_ind.add(possibleColumn.get(a));
                    level.get(possibleColumn.get(a)).add(String.valueOf(word.charAt(j)));
                    possibleColumn.remove(a);
                }
                Collections.sort(ans_ind);
                answers_words.add(word);
                answers_ind.add(ans_ind);
            }
        }

        public int getRandomInt(int max) {
            return (int) Math.floor(Math.random() * max);
        }

        public int size() {
            return level.size();
        }

        public ArrayList<String> get(int ind) {
            return level.get(ind);
        }
    }
}
