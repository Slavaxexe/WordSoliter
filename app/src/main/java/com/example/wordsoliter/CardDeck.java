package com.example.wordsoliter;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class CardDeck {
    LevelGenerator.Level level;
    Bitmap cardback;
    Bitmap cardfront;
    Bitmap emptycard;
    int h;

    public CardDeck(LevelGenerator.Level level){
        this.level = level;
    }

    public void setBitmaps(Bitmap cardback, Bitmap cardfront, Bitmap emptycard){
        this.cardback = cardback;
        this.cardfront = cardfront;
        this.emptycard = emptycard;
    }

    public void drawCard(Canvas canvas, Bitmap card, float x, float y, String letter, Paint paint){
        canvas.drawBitmap(card, x, y, paint);
        if (letter != null){
            Rect boundsText = new Rect();
            paint.getTextBounds(letter, 0, 1, boundsText);
            float xl = x + ((float) cardback.getWidth()  - boundsText.width()) / 2;
            float yl = y + ((float) cardfront.getHeight() + boundsText.height()) / 2;
            canvas.drawText(letter, xl, yl, paint);
        }
    }

    public void drawDeck(Canvas canvas, ArrayList<Integer> user_ind, int cardc){
        float x = 0;
        h = cardback.getHeight() / 6;
        int textsize = 150;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
            for (int i = 0; i < level.size(); i++) {
                ArrayList<String> column = level.get(i);
                int y = 0;
                for(int j = 0; j < column.size() - 1; j++){
                    drawCard(canvas, cardback, x, y, null, paint);
                    y += h;
                }
                if (!user_ind.contains(i) && level.get(i).size() != 0 && i != cardc)  drawCard(canvas, cardfront, x, y, level.get(i).get(level.get(i).size() - 1), paint);;
                x += (float) canvas.getWidth() / level.size();
            }
    }

    public void drawUserAnswer(Canvas canvas, String[] user_answer, int x){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        for (int i = 0; i < level.answers_words.get(level.answers_words.size() - 1).length(); i++){
            drawCard(canvas, emptycard, emptycard.getWidth() * i, cardfront.getHeight() * 3, null, paint);
        }
        for (int i = 0; i < user_answer.length - x; i++) {
            String letter = user_answer[i];
            if (!letter.equals("!"))
            drawCard(canvas, cardfront, cardfront.getWidth() * i, cardfront.getHeight() * 3, letter, paint);
        }
    }

    public int[] getCoordsEmptyCard(int i){

        return new int[]{cardfront.getWidth() * i, cardfront.getHeight() * 3};
    }

    public ArrayList<Integer> onTouch(float x, float y){
        int column = (int) (x / cardback.getWidth());
        ArrayList<Integer> a = new ArrayList<>();
        if((level.get(column).size() - 1) * (h) < y && (level.get(column).size() - 1) * (h) + cardback.getHeight() > y){
            a.add(column);
            a.add(column * cardback.getWidth());
            a.add((level.get(column).size() - 1) * (h));
        }
        else {
            a.add(-1);
        }
        return (a);
    }
    public int onUp(float x, float y){
        if (y > cardfront.getHeight() * 3 && y < cardfront.getHeight() * 4){
            if (x > 0 && x < emptycard.getWidth() * level.answers_words.get(level.answers_words.size() - 1).length()){
                return (int) (x / emptycard.getWidth());
            }
        }
        return -1;
    }


}
