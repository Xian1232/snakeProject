package com.gamecodeschool.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

class Snake {

    // The location in the grid of all the segments
    private ArrayList<Point> segmentLocations;

    // How big is each segment of the snake?
    private int mSegmentSize;

    // How big is the entire grid
    private Point mMoveRange;

    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    private enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    private Obstacle mObstacle;

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;


    Snake(Context context, Point mr, int ss) {

        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();

        // Initialize the segment size and movement
        // range from the passed in parameters
        mSegmentSize = ss;
        mMoveRange = mr;
        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x * ss / 2;
        initializeBitmaps(context);
        
    }

    private void initializeBitmaps(Context context) {
        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Modify the bitmaps to face the snake head
        // in the correct direction
        mBitmapHeadRight = Bitmap
                .createScaledBitmap(mBitmapHeadRight,
                        mSegmentSize, mSegmentSize, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, mSegmentSize, mSegmentSize, matrix, true);

        // Create and scale the body
        mBitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);

        mBitmapBody = Bitmap
                .createScaledBitmap(mBitmapBody,
                        mSegmentSize, mSegmentSize, false);
    }

    // Get the snake ready for a new game
    void reset(int width, int height) {

        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(width / 2, height / 2));
    }


    void move() {
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next segment
            // going forwards towards the head
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }

        // Move the head in the appropriate heading
        // Get the existing head position
        Point p = segmentLocations.get(0);

        // Move it appropriately
        switch (heading) {
            case UP:
                p.y--;
                break;

            case RIGHT:
                p.x++;
                break;

            case DOWN:
                p.y++;
                break;

            case LEFT:
                p.x--;
                break;
        }

    }

    void reverseMove() {
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next segment
            // going forwards towards the head
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }

        // Move the head in the appropriate heading
        // Get the existing head position
        Point p = segmentLocations.get(0);

        // Move it appropriately
        switch (heading) {
            case UP:
                p.y++;
                break;

            case RIGHT:
                p.x--;
                break;

            case DOWN:
                p.y--;
                break;

            case LEFT:
                p.x++;
                break;
        }

    }


    boolean detectDeath(Point oB) {
        // Has the snake died?
        return hitScreenEdge() || hitItself() || checkObstacle(oB);
    }
    private boolean hitScreenEdge() {
        // Hit any of the screen edges
        Point head = segmentLocations.get(0);
            return head.x == -1 || head.x > mMoveRange.x || head.y == -1 || head.y > mMoveRange.y;
    }

    private boolean hitItself() {
        // Eaten itself?
        Point head = segmentLocations.get(0);
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            Point body = segmentLocations.get(i);
            // Have any of the sections collided with the head
            if (head.x == body.x && head.y == body.y) {
                return true;
            }
        }
        return false;
    }

    boolean checkDinner(Point l) {
        //if (snakeXs[0] == l.x && snakeYs[0] == l.y) {
        if (segmentLocations.get(0).x == l.x &&
                segmentLocations.get(0).y == l.y) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the segment in front of it
            segmentLocations.add(new Point(-10, -10));
            return true;
        }
        return false;
    }

    boolean checkOrange(Point o) {
        // Same as checkDinner
        if (segmentLocations.get(0).x == o.x &&
                segmentLocations.get(0).y == o.y) {
            segmentLocations.add(new Point(-10, -10));
            segmentLocations.add(new Point(-10, -10));
            return true;
        }
        return false;
    }

    boolean checkBomb(Point b) {
        if (segmentLocations.get(0).x == b.x &&
                segmentLocations.get(0).y == b.y) {
            // Removes a segment when eaten
            segmentLocations.remove(0);
            return true;
        }
        return false;
    }

    boolean checkObstacle(Point oB) {

        if (segmentLocations.get(0).x == oB.x &&
                segmentLocations.get(0).y == oB.y) {
            return true;
        }
        return false;
    }

    void draw(Canvas canvas, Paint paint) {
        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // All the code from this method goes here
            // Draw the head
            Point head = segmentLocations.get(0);
            drawHead(canvas, paint, head);
            drawBody(canvas, paint);
        }
    }

    private void drawHead(Canvas canvas, Paint paint, Point head) {
        switch (heading) {
            case RIGHT:
                canvas.drawBitmap(mBitmapHeadRight,
                        head.x
                                * mSegmentSize,
                        head.y
                                * mSegmentSize, paint);
                break;

            case LEFT:
                canvas.drawBitmap(mBitmapHeadLeft,
                        head.x
                                * mSegmentSize,
                        head.y
                                * mSegmentSize, paint);
                break;

            case UP:
                canvas.drawBitmap(mBitmapHeadUp,
                        head.x
                                * mSegmentSize,
                        head.y
                                * mSegmentSize, paint);
                break;

            case DOWN:
                canvas.drawBitmap(mBitmapHeadDown,
                        head.x
                                * mSegmentSize,
                        head.y
                                * mSegmentSize, paint);
                break;
        }
    }

    private void drawBody(Canvas canvas, Paint paint) {
        // Draw the snake body one block at a time
        for (int i = 1; i < segmentLocations.size(); i++) {
            Point body = segmentLocations.get(i);
            canvas.drawBitmap(mBitmapBody,
                    body.x
                            * mSegmentSize,
                    body.y
                            * mSegmentSize, paint);
        }
    }


    // Handle changing direction
    void switchHeading(MotionEvent motionEvent) {

        // Is the tap on the right hand side?
        if (motionEvent.getX() >= halfWayPoint) {
            rotateRight();
        } else {
            rotateLeft();
        }
    }

    private void rotateRight() {
        switch (heading) {
            // Rotate right
            case UP:
                heading = Heading.RIGHT;
                break;
            case RIGHT:
                heading = Heading.DOWN;
                break;
            case DOWN:
                heading = Heading.LEFT;
                break;
            case LEFT:
                heading = Heading.UP;
                break;
        }
    }

    private void rotateLeft() {
        // Rotate left
        switch (heading) {
            case UP:
                heading = Heading.LEFT;
                break;
            case LEFT:
                heading = Heading.DOWN;
                break;
            case DOWN:
                heading = Heading.RIGHT;
                break;
            case RIGHT:
                heading = Heading.UP;
                break;
        }
    }
}
