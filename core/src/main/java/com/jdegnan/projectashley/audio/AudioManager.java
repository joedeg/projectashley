package com.jdegnan.projectashley.audio;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.jdegnan.projectashley.assets.Assets;

public class AudioManager {

    private Music currentMusic;

    private float musicVolume = 1.0f;

    private float soundVolume = 1.0f;

    public AudioManager() {}

    public void playMusic(Music music, boolean looping) {
        if(music == null) {
            return;
        }

        stopMusic();

        currentMusic = music;
        currentMusic.setLooping(looping);
        currentMusic.setVolume(musicVolume);
        currentMusic.play();

    }

    public void playMusic(Assets assets,
                          AssetDescriptor<Music> musicDescriptor,
                          boolean looping){

        Music music = assets.get(musicDescriptor);
        playMusic(music, looping);
    }

    public void stopMusic() {
        if(currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    public void playSound(Sound sound){
        if(sound == null){
            return;
        }

        sound.play(soundVolume);
    }

    public void playSound(
        Assets assets,
        AssetDescriptor<Sound> soundDescriptor){

        Sound sound = assets.get(soundDescriptor);
        playSound(sound);
    }

    public void setMusicVolume(float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if(currentMusic != null){
            currentMusic.setVolume(musicVolume);
        }
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float volume) {
        soundVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public void dispose(){
        stopMusic();
    }
}
