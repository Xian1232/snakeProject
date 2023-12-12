package com.gamecodeschool.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;


public class GameMenu {

    private Point screenSize;
    private Paint paint;
    private MenuItem newGameButton;
    private MenuItem achievementButton;
    private boolean isAchievementButtonClicked = false; // This is the new flag.
    private boolean achievementsInitialized = false;
    private Context context;
    private List<Achievement> achievements = new ArrayList<>();
    private MenuItem statsButton;
    private MenuItem skinsButton;
    private MenuItem playButton;
    private MenuItem pauseButton;
    private MenuItem closeButton;

    private float buttonWidth;
    private float buttonHeight;
    private float margin;

    public GameMenu(Point screenSize) {
        this.screenSize = screenSize;
        this.paint = new Paint();
        this.buttonWidth = 0;
        this.buttonHeight = 0;
        this.margin = 100;

        float centerX = screenSize.x - buttonWidth / 2 - margin;
        float centerY = screenSize.y / 2;

        this.newGameButton = new MenuItem("New Game", centerX - 400, centerY - 250, true);
        this.achievementButton = new MenuItem("Achievements", centerX - 400, centerY - 50, true);
        //this.statsButton = new MenuItem("Stats", centerX - 400, centerY + 150, true);
        //this.skinsButton = new MenuItem("Skins", centerX - 400, centerY + 350, true);
        this.playButton = new MenuItem("▷", screenSize.x - 50 - margin, 70, true);
        this.pauseButton = new MenuItem("⏸", screenSize.x - 50 - margin, 70, true);
        float closeButtonSize = 100; // Size of the close button
        this.closeButton = new MenuItem("X", screenSize.x - closeButtonSize / 2 - margin, closeButtonSize / 2 + margin, true);
    }

    public void draw(Canvas canvas, boolean isPaused, int highestScore, long highestTime, int pScore, long pTime) {
        paint.setColor(Color.argb(200, 0, 0, 0));
        canvas.drawRect(0, 0, screenSize.x, screenSize.y, paint);
        drawButton(canvas, newGameButton, 600, 130);
        drawButton(canvas, achievementButton, 600, 130);
        //drawButton(canvas, statsButton, 600, 130);
        //drawButton(canvas, skinsButton, 600, 130);

        if (isPaused) {
            drawButton(canvas, playButton, 75, 75);
        }
        if (isAchievementButtonClicked) {
            drawAchievements(canvas);
        }

        drawInformationBox(canvas, highestScore, highestTime, pScore, pTime);
    }

    private void drawButton(Canvas canvas, MenuItem button, int buttonWidth, int buttonHeight) {
        button.draw(canvas, paint, buttonWidth, buttonHeight);
    }
    private void initializeAchievements() {
        if (!achievementsInitialized) {

            String highScoreName = context.getResources().getString(R.string.achievement_high_score);
            String highScoreDesc = context.getResources().getString(R.string.achievement_5_score_desc);

            // Create an Achievement object with the retrieved strings
            Achievement highScoreAchievement = new Achievement("high_score", highScoreName, highScoreDesc);

            // Add the achievement to the list
            achievements.add(highScoreAchievement);

            // Add additional achievements as needed
            achievementsInitialized = true;
        }
    }
    private void drawAchievements(Canvas canvas) {
        float boxWidth = screenSize.x - 2 * margin;
        float boxHeight = screenSize.y / 2;
        float left = margin;
        float top = screenSize.y / 4;
        float right = screenSize.x - margin;
        float bottom = top + boxHeight;

        // Draw the achievements box background
        paint.setColor(Color.argb(255, 0, 128, 0));
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, 25, 25, paint);

        // Set up paint for the text
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER); // Center text horizontally

        // Calculate the starting y position for the achievements text
        float textY = top + paint.getTextSize() * 2; // Start below the top of the box

        // Draw the title for the achievements section
        canvas.drawText("Achievements", screenSize.x / 2, textY, paint);
        textY += paint.getTextSize() * 1.5;

        // Draw each achievement
        for (Achievement achievement : achievements) {
            String achievementText = achievement.getName() + " - " +
                    (achievement.isUnlocked() ? "Unlocked" : "Locked");
            canvas.drawText(achievementText, screenSize.x / 2, textY, paint);
            textY += paint.getTextSize() * 1.5; // Increment y position for the next achievement
        }
        closeButton.draw(canvas, paint, closeButton.getButtonWidth(), closeButton.getButtonHeight());
    }

    public void setAchievementButtonClicked(boolean clicked) {
        isAchievementButtonClicked = clicked;
    }

    private void drawInformationBox(Canvas canvas, int highestScore, long highestTime, int pScore, long pTime) {
        float boxWidth = 1000; // Adjust the width of the information box
        float boxHeight = 800; // Adjust the height of the information box
        float left = margin;
        float top = screenSize.y / 8;
        float right = left + boxWidth;
        float bottom = top + boxHeight;

        // Draw information box background
        paint.setColor(Color.argb(100, 20, 240, 0));
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, 10, 10, paint);

        // Draw information box text
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);

        // Center the text in the information box
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;
        float lineHeight = 100;


        // Example data, replace with your actual data
        canvas.drawText("Highest Score: " + highestScore, centerX, centerY - 2.5f * lineHeight, paint);
        canvas.drawText("Highest Time: " + formatTime(highestTime), centerX, centerY - 1.5f * lineHeight, paint);
        canvas.drawText("Previous Score: " + pScore, centerX, centerY + 1.5f * lineHeight, paint);
        canvas.drawText("Previous Time: " + formatTime(pTime), centerX, centerY + 2.5f * lineHeight, paint);
    }

    private String formatTime(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if (newGameButton.isTouched(x, y)) {
            return true;
        }
        else {
            if (achievementButton.isTouched(x, y)) {
                if (isAchievementButtonClicked && closeButton.isTouched(x, y)) {
                    isAchievementButtonClicked = false;
                    return false;
                }
                return false;
            }
            if (!isAchievementButtonClicked && achievementButton.isTouched(x, y)) {
                // Toggle the display of achievements
                isAchievementButtonClicked = !isAchievementButtonClicked;

                if (!achievementsInitialized) {
                    initializeAchievements();
                    achievementsInitialized = true;
                }
                return true;
            }

            // If none of the buttons were touched, return false
            return false;
        }

    }

    private static class MenuItem {
        private String text;
        private float x;
        private float y;
        private float textSize;
        private float buttonWidth = 600;
        private float buttonHeight = 130;
        private boolean hasBackground;

        public MenuItem(String text, float x, float y, boolean hasBackground) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.textSize = 80;
            this.hasBackground = hasBackground;
        }
        public float getButtonWidth() {
            return buttonWidth;
        }

        public float getButtonHeight() {
            return buttonHeight;
        }

        public void draw(Canvas canvas, Paint paint, float buttonWidth, float buttonHeight) {
            if (hasBackground) {
                paint.setColor(Color.argb(100, 20, 240, 0));
                float left = x - buttonWidth / 2;
                float top = y - buttonHeight / 2;
                float right = x + buttonWidth / 2;
                float bottom = y + buttonHeight / 2;
                RectF rect = new RectF(left, top, right, bottom);

                // Draw button background
                canvas.drawRoundRect(rect, 10, 10, paint);
            }

            // Draw button text
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float baseline = (y - fontMetrics.ascent + y - fontMetrics.descent) / 2;

            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, x, baseline, paint);
        }

        public boolean isTouched(float touchX, float touchY) {
            float left = x - buttonWidth / 2;
            float top = y - buttonHeight / 2;
            float right = x + buttonWidth / 2;
            float bottom = y + buttonHeight / 2;

            // Check if the touch is inside the rectangular touch area
            return touchX >= left && touchX <= right &&
                    touchY >= top && touchY <= bottom;
        }
    }
}
