package com.kreidev.cmpackagepipebomb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import static com.kreidev.cmpackagepipebomb.PackagePipebomb.resLoc;

public class PipebombRenderer extends EntityRenderer<PrimedPipebomb> {

    public static final PartialModel PIPEBOMB = PartialModel.of(resLoc("item/pipebomb"));
    public static final PartialModel PIPEBOMB_WHITE = PartialModel.of(resLoc("item/pipebomb_white"));

    public PipebombRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PrimedPipebomb entity, float entityYaw, float partialTicks, PoseStack ms, @NotNull MultiBufferSource buffer, int packedLight) {
        ms.pushPose();

        // Expand when about to explode
        int i = entity.getFuse();
        if ((float)i - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float f1 = 1.0F + f * 0.3F;
            ms.scale(f1, f1, f1);
        }

        ms.mulPose(Axis.XP.rotationDegrees(30));
        ms.mulPose(Axis.ZP.rotationDegrees(30));

        CachedBuffers.partial(PIPEBOMB, Blocks.AIR.defaultBlockState())
                .light(packedLight)
                .translate(0, -0.2f, -0.4f)
                .scale(0.4f)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()));

        CachedBuffers.partial(PIPEBOMB_WHITE, Blocks.AIR.defaultBlockState())
                .light(packedLight)
                .translate(0, -0.2f, -0.4f)
                .scale(0.40f)
                .color(255, 255, 255, i / 5 % 2 == 0 ? 200 : 0)
                .renderInto(ms, buffer.getBuffer(RenderType.translucent()));

        ms.popPose();

        super.render(entity, entityYaw, partialTicks, ms, buffer, packedLight);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PrimedPipebomb primedPipebomb) {
        return null;
    }

    public static void init() {}
}
