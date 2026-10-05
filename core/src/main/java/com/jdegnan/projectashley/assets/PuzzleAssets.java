package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;

public class PuzzleAssets {
    public static final AssetDescriptor<Sound> GATE_OPEN =
        new AssetDescriptor<Sound>(
            "audio/gate_open.wav",
            Sound.class
        );

    private PuzzleAssets(){}
}
