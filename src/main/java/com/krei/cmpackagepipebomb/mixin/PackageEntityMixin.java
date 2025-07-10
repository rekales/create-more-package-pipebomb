package com.krei.cmpackagepipebomb.mixin;

import com.krei.cmpackagepipebomb.PackagePipebomb;
import com.krei.cmpackagepipebomb.PrimedPipebomb;
import com.simibubi.create.content.logistics.box.PackageEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PackageEntity.class, remap = false)
public class PackageEntityMixin {

    @Inject(
            method = "dropAllDeathLoot",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/simibubi/create/content/logistics/box/PackageItem;getContents(Lnet/minecraft/world/item/ItemStack;)Lnet/neoforged/neoforge/items/ItemStackHandler;"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void dropAllDeathLoot(ServerLevel level, DamageSource pDamageSource, CallbackInfo ci, ItemStackHandler contents) {
        if (contents == null)
            return;

        if (!level.isClientSide()) {
            for (int i = 0; i < contents.getSlots(); i++) {
                ItemStack itemstack = contents.getStackInSlot(i);
                if (itemstack.isEmpty())
                    continue;

                if (PackagePipebomb.RIGGED_TNT_ITEM.isIn(itemstack)) {
                    itemstack.shrink(1);
                    Vec3 pos = ((PackageEntity) (Object) this).position();
                    PrimedTnt primedtnt = new PrimedTnt(level, pos.x(), pos.y(), pos.z(), null);
                    primedtnt.setFuse(20);
                    level.addFreshEntity(primedtnt);
                }

                if (PackagePipebomb.RIGGED_PIPEBOMB_ITEM.isIn(itemstack)) {
                    itemstack.shrink(1);
                    Vec3 pos = ((PackageEntity) (Object) this).position();
                    PrimedTnt primedPipebomb = new PrimedPipebomb(level, pos.x(), pos.y(), pos.z(), null);
                    primedPipebomb.setFuse(20);
                    level.addFreshEntity(primedPipebomb);
                }
            }
        }

    }
}
