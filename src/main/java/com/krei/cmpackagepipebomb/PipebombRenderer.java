package com.krei.cmpackagepipebomb;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.util.Mth;

public class PipebombRenderer extends ThrownItemRenderer<PrimedPipebomb> {

    public PipebombRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PrimedPipebomb entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int packedLight) {
        ms.pushPose();

        int i = entity.getFuse();
        if ((float)i - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float f1 = 1.0F + f * 0.3F;
            ms.scale(f1, f1, f1);
        }

        // TODO: White blink
        super.render(entity, entityYaw, partialTicks, ms, buffer, packedLight);

        ms.popPose();
    }
}
