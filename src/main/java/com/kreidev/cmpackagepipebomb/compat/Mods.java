package com.kreidev.cmpackagepipebomb.compat;

import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public enum Mods {
    PACKAGE_COURIERS("cmpackagecouriers", "2.1.0");

    private final String id;
    private final String minVersion;

    Mods() {
        this.id = Lang.asId(name());
        this.minVersion = "0.0.0";
    }

    Mods(String id) {
        this.id = id;
        this.minVersion = "0.0.0";
    }

    Mods(String id, String minVersion) {
        this.id = id;
        this.minVersion = minVersion;
    }

    public String id() {
        return id;
    }

    public ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }

    public Block getBlock(String id) {
        return BuiltInRegistries.BLOCK.get(rl(id));
    }

    public Item getItem(String id) {
        return BuiltInRegistries.ITEM.get(rl(id));
    }

    public boolean contains(ItemLike entry) {
        if (!isLoaded())
            return false;
        Item asItem = entry.asItem();
        return RegisteredObjectsHelper.getKeyOrThrow(asItem)
                .getNamespace()
                .equals(id);
    }

    /**
     * @return a boolean of whether the mod is loaded or not based on mod id and minimum version
     */
    public boolean isLoaded() {
        return ModList.get().getModContainerById(id)
                .map(container -> {
                    ArtifactVersion current = container.getModInfo().getVersion();
                    ArtifactVersion required = new DefaultArtifactVersion(minVersion);
                    return current.compareTo(required) >= 0;
                })
                .orElse(false);
    }

    /**
     * Simple hook to run code if a mod is installed
     * @param toRun will be run only if the mod is loaded
     * @return Optional.empty() if the mod is not loaded, otherwise an Optional of the return value of the given supplier
     */
    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (isLoaded())
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    /**
     * Simple hook to execute code if a mod is installed
     * @param toExecute will be executed only if the mod is loaded
     */
    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded()) {
            toExecute.get().run();
        }
    }
}
