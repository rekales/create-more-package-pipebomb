package com.kreidev.cmpackagepipebomb.mixin;

import com.kreidev.cmpackagepipebomb.PackagePipebomb;
import com.kreidev.cmpackagepipebomb.PackageSpawn;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.box.PackageEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PackageEntity.class, remap = false)
public class PackageEntityMixin {

    @Inject(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/simibubi/create/content/logistics/box/PackageItem;getContents(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraftforge/items/ItemStackHandler;"
            )
    )
    private void dropAllDeathLoot(DamageSource pDamageSource, CallbackInfo ci, @Local(name = "contents") ItemStackHandler contents) {
        if (contents == null)
            return;

        PackageEntity self = (PackageEntity) (Object) this;

        if (!self.level().isClientSide()) {
            for (int i = 0; i < contents.getSlots(); i++) {
                ItemStack itemstack = contents.getStackInSlot(i);
                if (itemstack.isEmpty())
                    continue;

                if (itemstack.getItem() instanceof PackageSpawn packageSpawn) {
                    itemstack.shrink(1);
                    Vec3 pos = self.position();
                    packageSpawn.spawnEntity(self.level(), pos.x(), pos.y(), pos.z());
                }
            }
        }
    }
}
