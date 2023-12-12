package com.gamecodeschool.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class Orange {

    // The location of the orange on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an orange
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the orange
    private Bitmap mBitmapOrange;

    /// Set up the orange in the constructor
    Orange(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an orange
        mSize = s;
        // Hide the orange off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapOrange = BitmapFactory.decodeResource(context.getResources(), R.drawable.orange);

        // Resize the bitmap
        mBitmapOrange = Bitmap.createScaledBitmap(mBitmapOrange, s, s, false);
    }

    // This is called every time an orange is eaten
    void spawn(){
        // Choose two random values and place the orange
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }
    void remove(){
        location.x = -1000;
        location.y = -1000;
    }

    // Let SnakeGame know where the orange is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the orange
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapOrange,
                location.x * mSize, location.y * mSize, paint);

    }

}
