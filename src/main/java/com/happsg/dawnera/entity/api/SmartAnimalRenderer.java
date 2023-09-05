package com.happsg.dawnera.entity.api;

import com.happsg.dawnera.entity.api.SmartAnimal;
import com.happsg.dawnera.entity.api.SmartAnimalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmartAnimalRenderer extends GeoEntityRenderer<SmartAnimal> {
    public SmartAnimalRenderer(EntityRendererProvider.Context renderManager,String name) {
        super(renderManager, new SmartAnimalModel(name));

    }

}
