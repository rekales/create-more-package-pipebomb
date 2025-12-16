package com.kreidev.cmpackagepipebomb.mixin;

import com.kreidev.cmpackagepipebomb.PackageSpawn;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.box.PackageItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings("InvokeAssignCanReplacedWithExpression")
@Mixin(value = PackageItem.class, remap = false)
public abstract class PackageItemMixin {

    @Inject(
            method = "open",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;")
    )
    private static void open(Level worldIn, Player playerIn, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(name = "contents") ItemStackHandler contents) {
        if (contents == null)
            return;

        Vec3 lookVec = playerIn.getLookAngle();
        Vec3 pos = playerIn.getEyePosition().add(lookVec.scale(1));

        if (!worldIn.isClientSide()) {
            for (int i = 0; i < contents.getSlots(); i++) {
                ItemStack itemstack = contents.getStackInSlot(i);
                if (itemstack.isEmpty())
                    continue;

                if (itemstack.getItem() instanceof PackageSpawn packageSpawn) {
                    itemstack.shrink(1);
                    packageSpawn.spawnEntity(worldIn, pos.x(), pos.y(), pos.z());
                }
            }
        }
    }

    @Inject(
            method = "appendHoverText",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lcom/simibubi/create/content/logistics/box/PackageItem;getContents(Lnet/minecraft/world/item/ItemStack;)Lnet/neoforged/neoforge/items/ItemStackHandler;"
            )
    )
    private void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci, @Local(name = "contents") ItemStackHandler contents) {
        if (contents == null)
            return;

        for (int i = 0; i < contents.getSlots(); i++) {
            ItemStack itemstack = contents.getStackInSlot(i);
            if (itemstack.isEmpty())
                continue;

            if (itemstack.getItem() instanceof PackageSpawn) {
                contents.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }
}
