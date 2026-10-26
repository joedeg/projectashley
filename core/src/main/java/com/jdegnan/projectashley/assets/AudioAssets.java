package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioAssets {

    private AudioAssets() {

    }

    public static final AssetDescriptor<Music> OVERWORLD_MUSIC =
        new AssetDescriptor<>(
            "audio/overworld.mp3",
            Music.class
        );

    public static final AssetDescriptor<Sound> SWORD_SWING =
        new AssetDescriptor<>(
            "audio/sword_swing.wav",
            Sound.class
        );

    public static final AssetDescriptor<Music> DUNGEON_MUSIC =
        new AssetDescriptor<>("audio/dungeon.mp3", Music.class);

}
