


package com.gamecodeschool.snake;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SnakeGame extends SurfaceView implements Runnable, Audio {

    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    // for playing sound effects
    private SoundPool mSP;
    private int mEat_ID = -1;

    private int mBomb_ID = -1;
    private int mCrashID = -1;


    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;
    void setBlocksHigh(int newBlocksHigh) {
        mNumBlocksHigh = newBlocksHigh;
    }

    // How many points does the player have
    private int mScore;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    // A snake ssss
    private Snake mSnake;

    private Apple mApple;
    private Orange mOrange;
    private Clock mClock;
    private Bomb mBomb;
    private Star mStar;

    private Obstacle mObstacle;
    
    private SoundManager mSoundManager;

    private Timer gameTimer;

    private Point mPauseButtonPosition;
    private int mPauseButtonSize;

    private GameMenu mMenu;
    private AchievementManager achievementManager;

    private int highestScore;
    private long highestTime;


    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Point size, Context context, SnakeActivity snakeGame) {
        super(context);

        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;
        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;

        mSoundManager = new SoundManager(context);
        mMenu = new GameMenu(size, this.getContext(),this);

        initializeDrawingObjects();


            descriptor = assetManager.openFd("bomb.wav");
            mBomb_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

        GameObjectFactory factory = new GameObjectFactory(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);



        // Call the constructors of our two game objects
        mApple = factory.createApple();
        mOrange = factory.createOrange();
        mBomb = factory.createBomb();
        mClock = factory.createClock();
        mStar = factory.createStar();
        mSnake = factory.createSnake();
        mObstacle = factory.createObstacle();

        mSnake = factory.createSnake();
        gameTimer = new Timer();
        mPauseButtonPosition = new Point(size.x - 200, 20);
        mPauseButtonSize = 150;

        highestScore = 0;
        highestTime = 0;
        achievementManager = new AchievementManager(context);
    }
    private void initializeDrawingObjects() {
        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();


        // Call the constructors of our two game objects
        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        mOrange = new Orange(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mBomb = new Bomb(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        mClock = new Clock(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        mStar = new Star(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

    }


    // Called to start a new game
    public void newGame() {

        // reset the snake
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the food ready for dinner
        mApple.spawn();
        mOrange.spawn();

        //Le bomba
        mBomb.spawn();

        //Add the powerups
        mClock.spawn();
        mStar.spawn();

        mObstacle.spawn();

        // Reset the mScore
        mScore = 0;

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
        gameTimer.reset();
    }


    // Handles the game loop
    @Override
    public void run() {
        while (mPlaying) {
            if(!mPaused) {
                if (updateRequired()) {
                    update();
                }
            }
            draw();
        }
    }

    @Override
    public void play() {
        // Check the game state and play the appropriate sound
        if (mSnake.checkDinner(mApple.getLocation())) {
            // Snake ate the apple
            mSoundManager.playEatSound();
        } else if (mSnake.detectDeath()) {
            // Snake crashed
            mSoundManager.playCrashSound();
        }
    }

    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        final long TARGET_FPS = 10;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }
        return false;
    }


    // Update all the game objects
    public void update() {

        // Move the snake
        mSnake.move();
        // Check for achievements related to score, time, or special events
        achievementManager.checkAchievements(this);
        // Did the head of the snake eat the apple?
        if(mSnake.checkDinner(mApple.getLocation())){
            // This reminds me of Edge of Tomorrow.
            // One day the apple will be ready!
            mApple.spawn();

            // Add to  mScore
            mScore = mScore + 1;
            mSoundManager.playEatSound();
        }

        if(mSnake.checkOrange(mOrange.getLocation())){
            // Similar to Apple, all the functions are the same
            mOrange.spawn();
            mScore = mScore + 3;
            mSoundManager.playEatSound();
        }

        // Did the head of the snake eat the bomb?
       if(mSnake.checkBomb(mBomb.getLocation())){
            // Similar to Apple, but you lose a point!
            mBomb.spawn();
            mScore = mScore - 1;
            mSoundManager.playBombSound();
        }
        // Clock
        if(mSnake.checkDinner(mClock.getLocation())){
            mClock.spawn();
            // Give double points
               mScore = mScore * 2;
            mSoundManager.playClockSound();
        }
        // Star
        if(mSnake.checkStar(mStar.getLocation())){
            mStar.spawn();
            mScore = mScore + 10;
            mSoundManager.playEatSound();
        }
        // Did the snake die?
        if (mSnake.detectDeath()) {
            mSoundManager.playCrashSound();
            mPaused = true;
            gameTimer.stop();
            gameTimer.die();

            long elapsedTime = gameTimer.getElapsedTime();
            if (elapsedTime > highestTime) {
                highestTime = elapsedTime;
            }

            if ((mScore > highestScore) && (mScore > 0)) {
                highestScore = mScore;
            }
        }
    }

    // Helper method to format time in MM:SS format
    private String formatTime(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Do all the drawing
    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Fill the screen with a color
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the size and color of the mPaint for the text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(120);

            // Draw the score
            mCanvas.drawText("" + mScore, 20, 120, mPaint);


            // Draw the fruit and the snake

            // Draw the elapsed time
            mCanvas.drawText("Time: " + formatTime(gameTimer.getElapsedTime()), (mCanvas.getWidth() - 650) / 2, 120, mPaint);

            // Draw the pause button
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(90);
            mCanvas.drawText(mPaused ? "▷" : "||", mPauseButtonPosition.x + 10, mPauseButtonPosition.y  + mPauseButtonSize / 2, mPaint);

           // Draw the assets
            mApple.draw(mCanvas, mPaint);
            mOrange.draw(mCanvas, mPaint);
            mBomb.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);
            mClock.draw(mCanvas, mPaint);
            mStar.draw(mCanvas, mPaint);
            mObstacle.draw(mCanvas, mPaint);

            // Draw some text while paused
            if(mPaused){

                // Set the size and color of the mPaint for the text
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(250);

                // Draw the message
                // We will give this an international upgrade soon
                //mCanvas.drawText("Tap To Play!", 200, 700, mPaint);
                /*mCanvas.drawText(getResources().
                                getString(R.string.tap_to_play),
                        200, 700, mPaint); */

                mMenu.draw(mCanvas, mPaused, highestScore, highestTime, mScore, gameTimer.getElapsedTime());

            }
            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (isTouchInsidePauseButton(motionEvent.getX(), motionEvent.getY())) {
                    togglePause();
                    return true;
                }

                if (mPaused && mMenu.handleTouchEvent(motionEvent)) {
                    mPaused = false;
                    newGame();
                    gameTimer.start();
                    return true;
                }



                // Let the Snake class handle the input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }

    // Helper method to check if the touch is inside the pause button
    private boolean isTouchInsidePauseButton(float x, float y) {
        return x >= mPauseButtonPosition.x &&
                x <= mPauseButtonPosition.x + mPauseButtonSize &&
                y >= mPauseButtonPosition.y &&
                y <= mPauseButtonPosition.y + mPauseButtonSize;
    }

    // Helper method to toggle the pause state
    private void togglePause() {
        mPaused = !mPaused;
        if (mPaused) {
            gameTimer.pause();
        } else {
            gameTimer.resume();
        }
    }

    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
        gameTimer.pause();
    }


    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        // could remove?
        if (!mPaused) {
            mNextFrameTime = System.currentTimeMillis();
            gameTimer.resume();
        }
        mThread.start();
    }
    private void showAchievements() {

        mMenu.setAchievementButtonClicked(true); 
    }

    public int getScore() {
        return mScore;
    }
