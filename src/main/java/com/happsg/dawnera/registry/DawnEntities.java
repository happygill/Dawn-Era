package com.happsg.dawnera.registry;

import com.happsg.dawnera.DawnEra;
import com.happsg.dawnera.entity.DimorphodonEntity;
import com.happsg.dawnera.entity.DimorphodonRenderer;
import com.happsg.dawnera.util.DawnRegistrate;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

import static net.minecraft.data.loot.BlockLootSubProvider.noDrop;

public class DawnEntities {
    public static void init(){}
    public static final EntityEntry<DimorphodonEntity> DIMORPHODON  = DawnEra.REGISTRATE
            .entity("dimorphodon",DimorphodonEntity::new,MobCategory.CREATURE)
            .renderer(()->DimorphodonRenderer::new)
            .properties(b-> b.setTrackingRange(64)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true))
            .properties(DimorphodonEntity::build)
            .attributes(DimorphodonEntity::setAttributes)
            .loot((registrateEntityLootTables, tEntityType) -> registrateEntityLootTables.add(tEntityType, noDrop()))
            .defaultSpawnEgg(5197768,16731904)
            .register();


}
