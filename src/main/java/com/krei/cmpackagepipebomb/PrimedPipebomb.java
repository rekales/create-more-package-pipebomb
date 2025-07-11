package com.krei.cmpackagepipebomb;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class PrimedPipebomb extends PrimedTnt implements ItemSupplier {
    public PrimedPipebomb(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    }

    public PrimedPipebomb(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(PackagePipebomb.PIPEBOMB_ENTITY.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (float) (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    public static PrimedPipebomb createEmpty(EntityType<? extends PrimedPipebomb> entityType, Level level) {
        return new PrimedPipebomb(entityType, level);
    }

    @Override
    public ItemStack getItem() {
        return PackagePipebomb.PIPEBOMB_ITEM.asStack();
    }

    @Override
    protected void explode() {
        this.level()
                .explode(
                        this,
                        Explosion.getDefaultDamageSource(this.level(), this),
                        null,
                        this.getX(),
                        this.getY(0.0625),
                        this.getZ(),
                        3F,
                        false,
                        Level.ExplosionInteraction.TNT
                );
    }

    // Copied from Projectile.class
    public Vec3 getMovementToShoot(double x, double y, double z, float velocity, float inaccuracy) {
        return new Vec3(x, y, z)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.0172275 * (double)inaccuracy),
                        this.random.triangle(0.0, 0.0172275 * (double)inaccuracy),
                        this.random.triangle(0.0, 0.0172275 * (double)inaccuracy)
                )
                .scale((double)velocity);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3 = this.getMovementToShoot(x, y, z, velocity, inaccuracy);
        this.setDeltaMovement(vec3);
        this.hasImpulse = true;
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((x + z) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        Vec3 vec3 = shooter.getKnownMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, shooter.onGround() ? 0.0 : vec3.y, vec3.z));
    }
}
