package com.katiforis.checkers.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.katiforis.checkers.R;

import static android.content.Context.AUDIO_SERVICE;


public class AudioPlayer {
    private SoundPool soundPool;
    private float actVolume, maxVolume, volume;
    private AudioManager audioManager;

    private boolean loaded;
    private boolean plays;

    private int clickPlayer;
    private int piecePlayer;
    private int popupPlayer;


    public AudioPlayer(Context context) {
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

    public void playClickButton() {
        if (loaded && !plays) {
            soundPool.play(clickPlayer, volume, volume, 1, 0, 1f);
        }
    }

    public void playPiece() {

        if (loaded && !plays) {
            soundPool.play(piecePlayer, volume, volume, 1, 0, 1f);
        }
    }

    public void playPopup() {

        if (loaded && !plays) {
            soundPool.play(popupPlayer, volume, volume, 1, 0, 1f);
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            audioManager = null;
        }
    }

    public void mute(boolean state) {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, state);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
    }
}
