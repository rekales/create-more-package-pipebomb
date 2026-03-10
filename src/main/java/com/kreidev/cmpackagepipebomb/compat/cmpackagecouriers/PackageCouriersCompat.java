package com.kreidev.cmpackagepipebomb.compat.cmpackagecouriers;

import com.kreidev.cmpackagecouriers.CourierTarget;
import com.kreidev.cmpackagecouriers.PackageCouriersApi;
import com.kreidev.cmpackagepipebomb.PackagePipebomb;
import com.kreidev.cmpackagepipebomb.PackageSpawn;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class PackageCouriersCompat {

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(PackageCouriersCompat::onCommonSetup);
    }

    private static void onCommonSetup(final FMLCommonSetupEvent event) {
        PackageCouriersApi.registerUnpackEffects(PackagePipebomb.RIGGED_PIPEBOMB_ITEM.get(), PackageCouriersCompat::spawnRigged);
        PackageCouriersApi.registerUnpackEffects(PackagePipebomb.RIGGED_TNT_ITEM.get(), PackageCouriersCompat::spawnRigged);
    }

    private static ItemStack spawnRigged(Level level, ItemStack stack, Vec3 planePos, CourierTarget target) {
        if (stack.getItem() instanceof PackageSpawn packageSpawn) {
            packageSpawn.spawnEntity(level, planePos.x, planePos.y, planePos.z);
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
