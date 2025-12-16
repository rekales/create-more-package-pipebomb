package com.kreidev.cmpackagepipebomb;

import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RiggedPipebomb extends Item implements PackageSpawn {
    public RiggedPipebomb(Properties properties) {
        super(properties);
    }

    @Override
    public Entity getSpawnedEntity(Level level, double x, double y, double z) {
        PrimedPipebomb primedPipebomb = new PrimedPipebomb(level, x, y, z, null);
        primedPipebomb.setFuse(15);
        return primedPipebomb;
    }

    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof PostboxBlockEntity postbox) {
            for (int i=0; i<postbox.inventory.getSlots(); i++) {
                if (postbox.inventory.getItem(i).isEmpty()) {
                    postbox.inventory.setStackInSlot(i, stack.copy());
                    stack.shrink(1);
                    if (context.getPlayer() != null) {
                        context.getPlayer().displayClientMessage(Component.translatable("item.cmpackagepipebomb.pipebomb_rigged.trapped").withStyle(style -> style.withColor(ChatFormatting.RED)), true);
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }

        return super.onItemUseFirst(stack, context);
    }

    // NOTE: maybe use a more generic PackageSpawnItem that uses a supplier?
}
