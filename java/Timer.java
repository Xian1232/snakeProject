package com.gamecodeschool.snakegame;

import android.os.Handler;
import java.util.ArrayList;
import java.util.List;

public class Timer {

    private long startTime;
    private boolean isRunning;
    private long elapsedTimePaused;
    private Handler handler;
    private Runnable runnable;
    private long timeOfDeath;


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

    public void executeCommand(Command command) {
        command.execute();
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
            elapsedTimePaused = System.currentTimeMillis() - startTime;
        }
    }

    public void resume() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTimePaused;
            isRunning = true;
            handler.post(runnable);
        }
    }

    public void die() {
        timeOfDeath = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        } else if (timeOfDeath > 0) {
            return timeOfDeath - startTime;
        } else {
            return elapsedTimePaused;
        }
    }

    public void reset() {
        startTime = 0;
        isRunning = false;
        elapsedTimePaused = 0;
        timeOfDeath = 0;
    }
}
