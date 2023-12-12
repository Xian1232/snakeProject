package com.gamecodeschool.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Clock {

    // The location of the apple on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the apple
    private Bitmap mBitmapClock;

    /// Set up the apple in the constructor
    Clock(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an apple
        mSize = s;
        // Hide the clock off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapClock = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock);

        // Resize the bitmap
        mBitmapClock = Bitmap.createScaledBitmap(mBitmapClock, s, s, false);
    }

    // This is called every time the clock is eaten
    void spawn(){
        // Choose two random values and place the clock
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    void remove(){
        location.x = -1000;
        location.y = -1000;
    }

    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the apple
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapClock,
                location.x * mSize, location.y * mSize, paint);

    }

}
