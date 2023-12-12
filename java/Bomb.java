package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class Bomb {

    // The location of the bomb on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an bomb
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the bomb
    private Bitmap mBitmapBomb;

    /// Set up the bomb in the constructor
    Bomb(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an bomb
        mSize = s;
        // Hide the bomb off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);

        // Resize the bitmap
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, s, s, false);
    }

    // This is called every time an bomb is eaten
    void spawn(){
        // Choose two random values and place the bomb
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Let SnakeGame know where the bomb is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the bomb
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapBomb,
                location.x * mSize, location.y * mSize, paint);

    }

}
