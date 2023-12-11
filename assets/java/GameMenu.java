package com.gamecodeschool.snake133.java;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

public class GameMenu {

    private Point screenSize;
    private Paint paint;
    private MenuItem newGameButton;
    private MenuItem achievementButton;
    private MenuItem playButton;

    private float buttonWidth;
    private float buttonHeight;
    private float margin;

    public GameMenu(Point screenSize) {
        this.screenSize = screenSize;
        this.paint = new Paint();
        this.buttonWidth  = 600;
        this.buttonHeight = 130;
        this.margin = 100;

        float centerX = screenSize.x - buttonWidth / 2 - margin;
        float centerY = screenSize.y / 2;

        this.newGameButton = new MenuItem("New Game", centerX, centerY - 100, true);
        this.achievementButton = new MenuItem("Achievements", centerX, centerY + 100, true);
        this.playButton = new MenuItem("▷", screenSize.x - 50 - margin, 70, false);
    }

    public void draw(Canvas canvas, boolean isPaused, int highestScore, long highestTime, int pScore, long pTime) {
        paint.setColor(Color.argb(200, 0, 0, 0));
        canvas.drawRect(0, 0, screenSize.x, screenSize.y, paint);
        drawButton(canvas, newGameButton);
        drawButton(canvas, achievementButton);

        if (isPaused) {
            drawButton(canvas, playButton);
        }
        drawInformationBox(canvas, highestScore, highestTime, pScore, pTime);
    }

    private void drawButton(Canvas canvas, MenuItem button) {
        button.draw(canvas, paint, buttonWidth, buttonHeight);
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
        } else {
            //  button clicked, handle achievements
            // You can open a settings menu or perform any other actions here
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
