package com.krei.cmpackagepipebomb;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RiggedTNT extends Item implements PackageSpawn{
    public RiggedTNT(Properties properties) {
        super(properties);
    }

    @Override
    public Entity getSpawnedEntity(Level level, double x, double y, double z) {
        PrimedTnt primedtnt = new PrimedTnt(level, x, y, z, null);
        primedtnt.setFuse(15);
        return primedtnt;
    }
}
