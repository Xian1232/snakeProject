package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class Star {

    // The location of the star on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an star
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the star
    private Bitmap mBitmapStar;

    /// Set up the star in the constructor
    Star(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an star
        mSize = s;
        // Hide the star off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapStar = BitmapFactory.decodeResource(context.getResources(), R.drawable.star);

        // Resize the bitmap
        mBitmapStar = Bitmap.createScaledBitmap(mBitmapStar, s, s, false);
    }

    // This is called every time an star is eaten
    void spawn(){
        // Choose two random values and place the star
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Let SnakeGame know where the star is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the star
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapStar,
                location.x * mSize, location.y * mSize, paint);

    }

}
