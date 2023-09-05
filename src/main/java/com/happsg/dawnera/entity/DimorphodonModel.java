package com.happsg.dawnera.entity;



import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static com.happsg.dawnera.DawnEra.asResource;

public class DimorphodonModel extends DefaultedEntityGeoModel<DimorphodonEntity> {

    public DimorphodonModel() {
        super(asResource("dimorphodon"));
    }

    @Override
    public ResourceLocation getTextureResource(DimorphodonEntity animatable) {
        return asResource("textures/entity/dimorphodon_"+ (animatable.isMale() ? "male":"female")+".png");
    }
}
