package com.example.wordsoliter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    GameSession gameSession;
    public int tier;

    public DrawView(Context context, int i) {
        super(context);
        getHolder().addCallback(this);
        tier = i;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameSession = new GameSession(getContext(), getHolder(), tier);
        gameSession.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // изменение размеров SurfaceView
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameSession.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                gameSession.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameSession.OnTouch(event);
        return true;
    }

    public void getHint() {
        gameSession.getHint();
    }
}
