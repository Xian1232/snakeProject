package com.gamecodeschool.snakegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import java.io.IOException;

public class SoundManager {

    // for playing sound effects
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;

    SoundManager(Context context) {
        initializeSoundPool(context);
        try {
            initializeSoundEffects(context);

        } catch (IOException e) {
            // Error
        }
    }
    private void initializeSoundPool(Context context) {
        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private void initializeSoundEffects(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor descriptor;

        // Prepare the sounds in memory
        descriptor = assetManager.openFd("get_apple.ogg");
        mEat_ID = mSP.load(descriptor, 0);

        descriptor = assetManager.openFd("snake_death.ogg");
        mCrashID = mSP.load(descriptor, 0);
    }

    void playEatSound() {
        // Play a sound
        mSP.play(mEat_ID, 1, 1, 0, 0, 1);
    }

    void playCrashSound() {
        // Pause the game ready to start again
        mSP.play(mCrashID, 1, 1, 0, 0, 1);
    }
}
