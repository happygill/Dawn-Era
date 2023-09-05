package com.happsg.dawnera.entity.api;



import com.happsg.dawnera.entity.api.SmartAnimal;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static com.happsg.dawnera.DawnEra.asResource;

public class SmartAnimalModel extends DefaultedEntityGeoModel<SmartAnimal> {

    final String name;
    public SmartAnimalModel(String name) {
        super(asResource(name));
        this.name = name;
    }

    @Override
    public ResourceLocation getTextureResource(SmartAnimal animatable) {
        return asResource("textures/entity/"+name+"_"+ (animatable.isMale() ? "male":"female")+".png");
    }
}
