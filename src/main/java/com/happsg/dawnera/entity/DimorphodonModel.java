package com.happsg.dawnera.entity;



import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static com.happsg.dawnera.DawnEra.asResource;

public class DimorphodonModel extends DefaultedEntityGeoModel<DimorphodonEntity> {

    public DimorphodonModel() {
        super(asResource("dimorphodon"));
    }


    @Override
    public ResourceLocation getModelResource(DimorphodonEntity animatable) {
        if(animatable.isChild())
            return asResource("geo/entity/dimorphodon_baby.geo.json");
        return super.getModelResource(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(DimorphodonEntity animatable) {
        if(animatable.isChild())
            return asResource("textures/entity/dimorphodon_baby.png");
        return asResource("textures/entity/dimorphodon_"+ (animatable.isMale() ? "male":"female")+".png");
    }
}
