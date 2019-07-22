package com.katiforis.checkers.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.katiforis.checkers.R;

import static android.content.Context.AUDIO_SERVICE;


public class AudioPlayer {
    private static AudioPlayer INSTANCE;
    private static SoundPool soundPool;
    private static float actVolume, maxVolume, volume;
    private static AudioManager audioManager;

    private static boolean loaded;
    private static boolean plays;

    private static int clickPlayer;
    private static int piecePlayer;
    private static int popupPlayer;

    public static AudioPlayer getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AudioPlayer.class) {
                INSTANCE = new AudioPlayer();
            }
        }

        return INSTANCE;
    }

    public static void playClickButton() {
        if (loaded && !plays) {
            soundPool.play(clickPlayer, volume, volume, 1, 0, 1f);
        }
    }

    public static void playPiece() {

        if (loaded && !plays) {
            soundPool.play(piecePlayer, volume, volume, 1, 0, 1f);
        }
    }

    public static void playPopup() {

        if (loaded && !plays) {
            soundPool.play(popupPlayer, volume, volume, 1, 0, 1f);
        }
    }

    public static void init(Context context) {
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        clickPlayer = soundPool.load(context, R.raw.click2, 1);
        piecePlayer = soundPool.load(context, R.raw.piece, 1);
        popupPlayer = soundPool.load(context, R.raw.popup, 1);
    }

    public static void mute(boolean state) {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, state);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
    }
}
