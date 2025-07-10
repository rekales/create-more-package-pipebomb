package com.krei.cmpackagepipebomb;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class PipebombRenderer extends ThrownItemRenderer<PrimedPipebomb> {

    public PipebombRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PrimedPipebomb entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.getFuse() / 5 % 2 == 0) {
            // Blink as white
            // NOTE: Temp
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, 0);
        } else {
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }
}
