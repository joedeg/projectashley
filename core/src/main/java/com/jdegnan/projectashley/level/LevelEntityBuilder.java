package com.jdegnan.projectashley.level;

public interface LevelEntityBuilder {
    void build(
        Level level,
        LevelRuntime runtime
    );
}
