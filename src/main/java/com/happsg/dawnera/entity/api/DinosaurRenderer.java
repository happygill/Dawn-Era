package com.happsg.dawnera.entity.api;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DinosaurRenderer extends GeoEntityRenderer<DinosaurEntity> {
    public DinosaurRenderer(EntityRendererProvider.Context renderManager, String name, float shadow) {
        super(renderManager, new DinosaurModel(name));
        this.shadowRadius = shadow;
    }

    @Override
    public float getMotionAnimThreshold(DinosaurEntity animatable) {
        return 0.00001F;
    }
}
