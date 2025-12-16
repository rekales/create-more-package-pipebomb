package com.kreidev.cmpackagepipebomb;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;


@Mod(PackagePipebomb.MOD_ID)
public class PackagePipebomb {
    public static final String MOD_ID = "cmpackagepipebomb";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate
            .create(MOD_ID)
            .defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey() != null ?
                    AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey() : CreativeModeTabs.REDSTONE_BLOCKS);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public static final ItemEntry<PipebombItem> PIPEBOMB_ITEM = REGISTRATE
            .item("pipebomb", PipebombItem::new)
            .register();

    public static final ItemEntry<RiggedPipebomb> RIGGED_PIPEBOMB_ITEM = REGISTRATE
            .item("pipebomb_rigged", RiggedPipebomb::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<RiggedTNT> RIGGED_TNT_ITEM = REGISTRATE
            .item("tnt_rigged", RiggedTNT::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final EntityEntry<PrimedPipebomb> PIPEBOMB_ENTITY = REGISTRATE
            .entity("pipebomb", PrimedPipebomb::createEmpty, MobCategory.MISC)
            .properties(p -> p
                    .fireImmune()
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(10)
                    .updateInterval(10))
//        .renderer(() -> DeliveryPlaneRenderer::new)
            .register();

    public PackagePipebomb() {
        @SuppressWarnings("removal")
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(PackagePipebomb::clientInit);
        MinecraftForge.EVENT_BUS.addListener(PackagePipebomb::onRightClickedBlock);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PipebombRenderer.init();
        // Somethings wrong with registrate that makes me want to commit seppuku
        EntityRenderers.register(
                PIPEBOMB_ENTITY.get(),
                PipebombRenderer::new
        );
    }

    public static void onRightClickedBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.getBlockEntity(event.getPos()) instanceof PostboxBlockEntity postbox) {
            boolean spawnedBombs = false;
            for (int i=0; i<postbox.inventory.getSlots(); i++) {
                if (postbox.inventory.getItem(i).is(RIGGED_PIPEBOMB_ITEM.get())) {
                    Vec3 loc = event.getPos().getCenter();
                    RIGGED_PIPEBOMB_ITEM.get().spawnEntity(level, loc.x(), loc.y()+0.5f, loc.z());
                    postbox.inventory.setStackInSlot(i, ItemStack.EMPTY);
                    spawnedBombs = true;
                }
            }

            if (spawnedBombs) {
                event.setCancellationResult(InteractionResult.CONSUME);
                event.setCanceled(true);
            }
        }
    }

    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
