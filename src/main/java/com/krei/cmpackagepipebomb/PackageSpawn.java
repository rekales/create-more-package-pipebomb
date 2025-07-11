package com.krei.cmpackagepipebomb;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface PackageSpawn {
    Entity getSpawnedEntity(Level level, double x, double y, double z);

    default void spawnEntity(Level level, double x, double y, double z) {
        level.addFreshEntity(this.getSpawnedEntity(level, x, y, z));
    }
}
