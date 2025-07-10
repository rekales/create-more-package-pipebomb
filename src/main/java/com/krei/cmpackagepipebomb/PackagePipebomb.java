package com.krei.cmpackagepipebomb;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(PackagePipebomb.MODID)
public class PackagePipebomb {
    public static final String MODID = "cmpackagepipebomb";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MODID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey());

    public PackagePipebomb(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

    }
}
