package com.gamecodeschool.snakegame;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    private Context applicationContext;
    private SharedPreferences prefs;
    private List<Achievement> achievements = new ArrayList<>();
    private Achievement highScoreAchievement = new Achievement("high_score", "High Scorer", "Score over 5 points");

    public AchievementManager(Context context) {
        this.applicationContext = context.getApplicationContext();
        prefs = context.getSharedPreferences("Achievements", Context.MODE_PRIVATE);
        loadAchievements(context);
        achievements.add(highScoreAchievement);
    }

    private void loadAchievements(Context context) {

        String highScoreName = context.getResources().getString(R.string.achievement_high_score);
        String highScoreDesc = context.getResources().getString(R.string.achievement_5_score_desc);

        boolean isHighScoreUnlocked = prefs.getBoolean("high_score_unlocked", false);

        Achievement highScoreAchievement = new Achievement("high_score", highScoreName, highScoreDesc);
        highScoreAchievement.setUnlocked(isHighScoreUnlocked);
        
        achievements.add(highScoreAchievement);
    }

    public void checkAchievements(SnakeGame game) {
        // Check if any achievements should be unlocked
        if (game.getScore() > 5 && !highScoreAchievement.isUnlocked()) {
            unlockAchievement(highScoreAchievement);
            showAchievementUnlockedUI(highScoreAchievement);
        }
    }

        private void unlockAchievement (Achievement achievement ){
            // Unlock the achievement and save the state
            // ...
            achievement.setUnlocked(true);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(achievement.getId(), true);
            editor.apply();
            showAchievementUnlockedUI(achievement);
        }
    private void showAchievementUnlockedUI(Achievement achievement) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(applicationContext, "Unlocked: " + achievement.getName(), Toast.LENGTH_SHORT).show());

    }


}