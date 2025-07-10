package com.krei.cmpackagepipebomb.mixin;

import com.krei.cmpackagepipebomb.PackagePipebomb;
import com.krei.cmpackagepipebomb.PrimedPipebomb;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.box.PackageItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PackageItem.class, remap = false)
public abstract class PackageItemMixin {

    @Inject(
            method = "open",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void open(Level worldIn, Player playerIn, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, ItemStack box, ItemStackHandler contents) {
        if (contents == null)
            return;

        if (!worldIn.isClientSide()) {
            for (int i = 0; i < contents.getSlots(); i++) {
                ItemStack itemstack = contents.getStackInSlot(i);
                if (itemstack.isEmpty())
                    continue;

                if (PackagePipebomb.RIGGED_TNT_ITEM.isIn(itemstack)) {
                    itemstack.shrink(1);
                    PrimedTnt primedtnt = new PrimedTnt(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), null);
                    primedtnt.setFuse(20);
                    worldIn.addFreshEntity(primedtnt);
                }

                if (PackagePipebomb.RIGGED_PIPEBOMB_ITEM.isIn(itemstack)) {
                    itemstack.shrink(1);
                    PrimedTnt primedPipebomb = new PrimedPipebomb(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), null);
                    primedPipebomb.setFuse(20);
                    worldIn.addFreshEntity(primedPipebomb);
                }
            }
        }
    }
}
