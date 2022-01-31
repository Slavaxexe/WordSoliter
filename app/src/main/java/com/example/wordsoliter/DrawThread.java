package com.example.wordsoliter;


import static java.lang.Math.asin;
import static java.lang.Math.max;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    private Paint textPaint = new Paint();
    private Levels levels;
    private int levelId;
    private float x, y;
    private volatile boolean isGameStarted = false, isGameFinished = true;
    private ArrayList<ArrayList<String>> level;
    private ArrayList<String> user_answer, answers_word;
    private ArrayList<ArrayList<Integer>> answers_ind;
    private ArrayList<Integer> user_ind;
    private Context context;

    {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }




    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        user_answer = new ArrayList<>();
        answers_word = new ArrayList<>();
        user_ind = new ArrayList<>();
        answers_ind = new ArrayList<>();
        this.context = context;
        this.surfaceHolder = surfaceHolder;
    }



    public void requestStop() {
        running = false;
    }

    public static int getRandomInt(int max) {
        return (int) Math.floor(Math.random() * max);
    }

    public void touchDetector(float x, float y){
        if (isGameStarted && !isGameFinished){
            this.x = x;
            this.y = y;
            if (y > textPaint.getTextSize() && y < textPaint.getTextSize() * 2){
                int ind = (int) (x / textPaint.getTextSize());
                String a = level.get(ind).get(level.get(ind).size() - 1);
                user_answer.add(a);
                user_ind.add(ind);
                if (user_answer.size() == answers_word.get(answers_word.size() - 1).length()){
                    boolean f = true;
                    for(int i = 0; i < user_answer.size(); i++){
                        if (user_answer.get(i).charAt(0) != answers_word.get(answers_word.size() - 1).charAt(i)){
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        Collections.sort(user_ind);
                        if (!user_ind.equals(answers_ind.get(answers_ind.size() - 1))) {
                            Toast.makeText(context, "Попробуй другие буквы", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Правильно!", Toast.LENGTH_SHORT).show();
                            answers_word.remove(answers_word.size() - 1);
                            answers_ind.remove(answers_ind.size() - 1);
                            for (int i = 0; i < user_ind.size(); i++){
                                level.get(user_ind.get(i)).remove(level.get(user_ind.get(i)).size() - 1);
                            }
                            if (answers_word.size() == 0){
                                Toast.makeText(context, "Вы победили!", Toast.LENGTH_SHORT).show();
                                isGameFinished = true;
                            }
                        }
                    }
                    else {
                        Toast.makeText(context, "Неправильно!", Toast.LENGTH_SHORT).show();
                    }
                    user_answer.clear();
                    user_ind.clear();
                }
            }
        }
    }


    @Override
    public void run() {
        {//Создание уровня
            levelId = 0;
            levels = new Levels();
            level = new ArrayList<>(levels.getCountColumn(levelId));
            for (int i = 0; i < levels.getCountColumn(levelId); i++)
                level.add(new ArrayList<String>());
            for (String word : levels.getLevel(levelId)) {
                ArrayList<Integer> ans_ind = new ArrayList<>();
                ArrayList<Integer> isfiled = new ArrayList<>(levels.getCountColumn(levelId));
                for (int i = 0; i < levels.getCountColumn(levelId); i++) isfiled.add(i);
                for (int j = 0; j < word.length(); j++) {
                    int a = getRandomInt(isfiled.size());
                    ans_ind.add(isfiled.get(a));
                    level.get(isfiled.get(a)).add(String.valueOf(word.charAt(j)));
                    isfiled.remove(a);
                }
                Collections.sort(ans_ind);
                answers_ind.add(ans_ind);
            }
            for (int i = 0; i < level.size(); i++) {
                for (int j = 0; j < level.get(i).size(); j++) {
                    System.out.print(level.get(i).get(j) + " ");
                }
                System.out.println();
            }
            {
                Canvas canvas = surfaceHolder.lockCanvas();
                textPaint.setTextSize(canvas.getWidth() / (level.size()));
                textPaint.setStyle(Paint.Style.FILL);
                surfaceHolder.unlockCanvasAndPost(canvas);

            }
            answers_word = levels.getLevel(levelId);
        }
        isGameStarted = true;
        isGameFinished = false;
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    for(int i = 0; i < level.size(); i++){
                        canvas.drawText(max(0, level.get(i).size() - 1) + "", i * textPaint.getTextSize(), textPaint.getTextSize(), textPaint);
                    }
                    for(int i = 0; i < level.size(); i++){
                        if (level.get(i).size() > 0){
                            canvas.drawText(level.get(i).get(level.get(i).size() - 1), i * textPaint.getTextSize(), textPaint.getTextSize() * 2, textPaint);
                        }
                    }
                    for(int i = 0; i < user_answer.size(); i++){
                        canvas.drawText(user_answer.get(i), i * textPaint.getTextSize(), textPaint.getTextSize() * 4, textPaint);
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}