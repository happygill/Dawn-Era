package com.happsg.dawnera.registry;

import com.happsg.dawnera.entity.api.DietBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

import com.happsg.dawnera.entity.api.DietBuilder.Diet;

public final class AllAnimalDiets {

    public static final Diet DIMORPHODAN_DIET = DietBuilder.diet()
            .addFoodItems(Items.COOKED_CHICKEN,Items.COOKED_RABBIT,Items.CHICKEN,Items.RABBIT)
            .addFoodEntities(EntityType.CHICKEN,EntityType.RABBIT,EntityType.PARROT,EntityType.BAT,EntityType.SILVERFISH)
            .build();

    public static final Diet DIABLOCERATOPS_DIET = DietBuilder.diet()
            .addFoodItems(Items.BAKED_POTATO, Items.BEETROOT, Items.MELON_SLICE, Items.CARROT, Items.APPLE, Items.WHEAT, Items.SWEET_BERRIES).build();

    public static final Diet EMPTY_DIET = DietBuilder.diet().hasHungerDrain(false)
            .build();
}
