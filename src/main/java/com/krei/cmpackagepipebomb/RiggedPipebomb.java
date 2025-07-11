package com.krei.cmpackagepipebomb;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RiggedPipebomb extends Item implements PackageSpawn {
    public RiggedPipebomb(Properties properties) {
        super(properties);
    }

    @Override
    public Entity getSpawnedEntity(Level level, double x, double y, double z) {
        PrimedPipebomb primedPipebomb = new PrimedPipebomb(level, x, y, z, null);
        primedPipebomb.setFuse(20);
        return primedPipebomb;
    }
}
