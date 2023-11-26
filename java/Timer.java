package com.gamecodeschool.snakegame;

import android.os.Handler;

public class Timer {

    private long startTime;
    private boolean isRunning;
    private long elapsedTimePaused;

    private Handler handler;
    private Runnable runnable;

    public Timer() {
        startTime = 0;
        isRunning = false;
        handler = new Handler();
        initializeRunnable();
    }

    private void initializeRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                handler.postDelayed(this, 1000); // Update every 1 second
            }
        };
    }

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
            handler.post(runnable);
        }
    }

    public void stop() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    public void pause() {
        if (isRunning) {
            stop();
            elapsedTimePaused += System.currentTimeMillis() - startTime;
        }
    }


    public long getElapsedTime() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        } else {
            return 0;
        }
    }

    public void reset() {
        startTime = 0;
        isRunning = false;
    }
}
