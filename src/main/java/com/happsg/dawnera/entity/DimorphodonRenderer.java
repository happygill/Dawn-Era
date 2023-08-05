package com.happsg.dawnera.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DimorphodonRenderer extends GeoEntityRenderer<DimorphodonEntity> {
    public DimorphodonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DimorphodonModel());

    }

}
