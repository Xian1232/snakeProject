package com.gamecodeschool.snakegame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import java.io.IOException;

// Define the Audio interface
interface Audio {
    void play();
}

// Concrete implementation for playing eat sound
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

// Concrete implementation for playing crash sound
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

class BombSound implements Audio {
    private final SoundPool soundPool;
    private final int soundId;

    BombSound(SoundPool soundPool, int soundId) {
        this.soundPool = soundPool;
        this.soundId = soundId;
    }

    @Override
    public void play() {
        soundPool.play(soundId, 1, 1, 0, 0, 1);
    }
}

// Context for Audio, allows switching between different audio implementations
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

// SoundManager now uses the Audio and AudioContext classes
public class SoundManager {
    private SoundPool soundPool;
    private int eatSoundId = -1;
    private int bombId = -1;

    private int crashSoundId = -1;
    private AudioContext audioContext;

    SoundManager(Context context) {
        initializeSoundPool(context);
        try {
            initializeSoundEffects(context);
        } catch (IOException e) {
            // Handle error
        }

        // Initialize the default audio context
        audioContext = new AudioContext();
        audioContext.setAudio(new EatSound(soundPool, eatSoundId)); // Set default to EatSound
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

        descriptor = assetManager.openFd("bomb.wav");
        bombId = soundPool.load(descriptor, 0);

        descriptor = assetManager.openFd("snake_death.ogg");
        crashSoundId = soundPool.load(descriptor, 0);
    }

    void playEatSound() {
        audioContext.setAudio(new EatSound(soundPool, eatSoundId));
        audioContext.playAudio();
    }

    void playCrashSound() {
        audioContext.setAudio(new CrashSound(soundPool, crashSoundId));
        audioContext.playAudio();
    }

    void playBombSound() {
        audioContext.setAudio(new BombSound(soundPool, crashSoundId));
        audioContext.playAudio();
    }
}
