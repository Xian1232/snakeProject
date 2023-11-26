package com.gamecodeschool.snakegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import java.io.IOException;

interface Audio {
    void play();
}

class EatSound implements Audio {
    private final SoundPool soundPool;
    private final int soundId;

    EatSound(SoundPool soundPool, int soundId) {
        this.soundPool = soundPool;
        this.soundId = soundId;
    }

    @Override
    public void play() {
        soundPool.play(soundId, 1, 1, 0, 0, 1);
    }
}

class CrashSound implements Audio {
    private final SoundPool soundPool;
    private final int soundId;

    CrashSound(SoundPool soundPool, int soundId) {
        this.soundPool = soundPool;
        this.soundId = soundId;
    }

    @Override
    public void play() {
        soundPool.play(soundId, 1, 1, 0, 0, 1);
    }
}

class AudioContext {
    private Audio audio;

    void setAudio(Audio audio) {
        this.audio = audio;
    }

    void playAudio() {
        if (audio != null) {
            audio.play();
        }
    }
}

public class SoundManager {

    // for playing sound effects
    private SoundPool soundPool;
    private int eatSoundId = -1;
    private int crashSoundId = -1;
    private AudioContext audioContext;

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

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private void initializeSoundEffects(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor descriptor;

        // Prepare the sounds in memory
        descriptor = assetManager.openFd("get_apple.ogg");
        eatSoundId = soundPool.load(descriptor, 0);

        descriptor = assetManager.openFd("snake_death.ogg");
        crashSoundId = soundPool.load(descriptor, 0);
    }

    void playEatSound() {
        // Play a sound
        audioContext.setAudio(new EatSound(soundPool, eatSoundId));
        audioContext.playAudio();
    }

    void playCrashSound() {
        // Pause the game ready to start again
        audioContext.setAudio(new CrashSound(soundPool, crashSoundId));
        audioContext.playAudio();
    }
}
