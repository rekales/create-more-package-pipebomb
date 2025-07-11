package com.krei.cmpackagepipebomb.mixin;

import com.krei.cmpackagepipebomb.PackageSpawn;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

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
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci, int visibleNames, int skippedNames, ItemStackHandler contents) {
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
