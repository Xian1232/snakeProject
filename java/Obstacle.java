package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class Obstacle {

    // The location of the obstacle on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an obstacle
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the obstacle
    private Bitmap mBitmapObstacle;

    /// Set up the obstacle in the constructor
    Obstacle(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an obstacle
        mSize = s;
        // Hide the obstacle off-screen until the game starts
        location.x = -10;

        // Load the image to the bitmap
        mBitmapObstacle = BitmapFactory.decodeResource(context.getResources(), R.drawable.obstacle);

        // Resize the bitmap
        mBitmapObstacle = Bitmap.createScaledBitmap(mBitmapObstacle, s, s, false);
    }

    void spawn(){
        // Choose two random values and place the obstacle
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Let SnakeGame know where the obstacle is
    // SnakeGame can share this with the snake
    Point getLocation(){
        return location;
    }

    // Draw the obstacle
    void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapObstacle,
                location.x * mSize, location.y * mSize, paint);

    }

}
