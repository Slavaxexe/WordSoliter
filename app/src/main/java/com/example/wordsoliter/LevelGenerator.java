package com.example.wordsoliter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class LevelGenerator {
    private final ArrayList<String> dictWords = new ArrayList<>();

    public LevelGenerator(ArrayList<String> dictionary) {
        for (String line : dictionary) {
            dictWords.add(line.split(" ")[0]);
        }
    }

    public Level generateLevel(int tier) {
        int l = 0, r = 2000;
        int countWords = 10;
        int minLenWord = 4;
        int maxLenWord = 8;
        int[] dictboards = {0, 2000, 10377, 14421, 93421};
        switch (tier) {
            case (1):
                l = dictboards[0];
                r = dictboards[1];
                break;
            case (2):
                l = dictboards[1];
                r = dictboards[2];
                break;
            case (3):
                l = dictboards[2];
                r = dictboards[3];
                break;
            case (4):
                l = dictboards[3];
                r = dictboards[4];
                break;
        }
        ArrayList<String> dictForLevel = new ArrayList<>(dictWords.subList(l, r));
        dictForLevel = dictForLevel.stream().filter(x -> x.length() <= maxLenWord && x.length() >= minLenWord && x.length() != 7).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(dictForLevel);
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < countWords; i++) {
            words.add(dictForLevel.get(i));
        }
        return new Level(words);
    }


    static class Level {
        ArrayList<ArrayList<Integer>> answers_ind;
        ArrayList<ArrayList<String>> level;
        ArrayList<String> answers_words;

        Level(ArrayList<String> words) {
            int CountColumn = words.get(0).length();

            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).length() > CountColumn) {
                    CountColumn = words.get(i).length();
                }
            }
            level = new ArrayList<>();
            answers_ind = new ArrayList<>();
            answers_words = new ArrayList<>();
            for (int i = 0; i < CountColumn; i++)
                level.add(new ArrayList<>());
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
