package com.gamecodeschool.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

class SnakeGame extends SurfaceView implements Runnable {
    // Game loop
    // /thread control
    private Thread gameThread = null;
    private long nextFrameTime;
    private volatile boolean isPlaying = false;
    private volatile boolean isPaused = true;

    // Sound effects
    private SoundPool soundPool;
    private int eatSoundId = -1;
    private int crashSoundId = -1;

    // Playable area size
    private static final int NUM_BLOCKS_WIDE = 40;
    private int numBlocksHigh;

    // Player score
    private int score;

    // Drawing objects
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Paint paint;

    // Game objects
    private Snake snake;
    private Apple apple;

    // Constructor
    public SnakeGame(Context context, Point size) {
        super(context);
        initializeGame(context, size);
    }

    // Initialize game elements
    private void initializeGame(Context context, Point size) {
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        numBlocksHigh = size.y / blockSize;

        initializeSound(context);
        initializeDrawingObjects();
        initializeGameObjects(context, blockSize);
    }

    // Initialize SoundPool and load sounds
    private void initializeSound(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            eatSoundId =  soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            crashSoundId =  soundPool.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }

    }

    // Initialize drawing objects
    private void initializeDrawingObjects() {
        surfaceHolder = getHolder();
        paint = new Paint();
    }

    // Initialize game objects
    private void initializeGameObjects(Context context, int blockSize) {

        apple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        numBlocksHigh),
                blockSize);

        snake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        numBlocksHigh),
                blockSize);


    }

    // Start a new game
    public void newGame() {
        resetGame();
        scheduleNextUpdate();
    }

    // Reset game
    private void resetGame() {
        snake.reset(NUM_BLOCKS_WIDE, numBlocksHigh);
        apple.spawn();
        score = 0;
    }

    // Schedule the next game update
    private void scheduleNextUpdate() {
        nextFrameTime = System.currentTimeMillis();
    }

    // Game loop
    @Override
    public void run() {
        while (isPlaying) {
            if (!isPaused && updateRequired()) {
                update();
            }
            drawGame();
        }
    }

    // Check if an update is required
    private boolean updateRequired() {
        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(nextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            nextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }



    // Update game state
    // Update all the game objects
    public void update() {

        // Move the snake
        snake.move();

        // Did the head of the snake eat the apple?
        if(snake.checkDinner(apple.getLocation())){
            // This reminds me of Edge of Tomorrow.
            // One day the apple will be ready!
            apple.spawn();

            // Add to  Score
            score = score + 1;

            // Play a sound
            soundPool.play(eatSoundId, 1, 1, 0, 0, 1);
        }

        // Did the snake die?
        if (snake.detectDeath()) {
            // Pause the game ready to start again
            soundPool.play(crashSoundId, 1, 1, 0, 0, 1);

            isPaused =true;
        }

    }





    // Draw game elements
    private void drawGame() {

        // Get a lock on the mCanvas
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Fill the screen with a color
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the size and color of the mPaint for the text
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(120);

            // Draw the score
            canvas.drawText("" + score, 20, 120, paint);

            // Draw the apple and the snake
            apple.draw(canvas, paint);
            snake.draw(canvas, paint);

            // Draw some text while paused
            if(isPaused){

                // Set the size and color of the mPaint for the text
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(250);

                // Draw the message
                // We will give this an international upgrade soon
                canvas.drawText("Tap To Play!", 200, 700, paint);

            }


            // Unlock the mCanvas and reveal the graphics for this frame
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }



    // Handle touch events
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (isPaused) {
                    isPaused = false;
                    newGame();

                    // Don't want to process snake direction for this tap
                    return true;
                }

                // Let the Snake class handle the input
                snake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }


    // Pause the game
    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Handle error
        }
    }

    // Resume the game
    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
