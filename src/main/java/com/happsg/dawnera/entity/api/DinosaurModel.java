package com.happsg.dawnera.entity.api;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import static com.happsg.dawnera.DawnEra.asResource;

public class DinosaurModel extends DefaultedEntityGeoModel<DinosaurEntity> {
    final String name;

    public DinosaurModel(String name) {
        super(asResource(name), true);
        this.name = name;
    }

    @Override
    public ResourceLocation getTextureResource(DinosaurEntity animatable) {
        return asResource("textures/entity/" + name + "/" + name + "_" + (animatable.isMale() ? "male" : "female") + ".png");
    }

    @Override
    public ResourceLocation getModelResource(DinosaurEntity animatable) {
        return asResource(animatable.isBaby() ? "geo/entity/" + name + "_baby.geo.json" : "geo/entity/" + name + ".geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(DinosaurEntity animatable) {
        return asResource(animatable.isBaby() ? "animations/entity/" + name + "_baby.animation.json" : "animations/entity/" + name + ".animation.json");
    }

    @Override
    public void setCustomAnimations(DinosaurEntity animatable, long instanceId, AnimationState<DinosaurEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
