package com.happsg.dawnera.registry;

import com.happsg.dawnera.DawnEra;
import com.happsg.dawnera.entity.animals.*;
import com.happsg.dawnera.entity.api.DinosaurRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

import static net.minecraft.data.loot.BlockLootSubProvider.noDrop;

public class AllEntities {
    public static void init(){}

    public static final EntityEntry<DimorphodonEntity> DIMORPHODON  = DawnEra.REGISTRATE
            .entity("dimorphodon",DimorphodonEntity::new,MobCategory.CREATURE)
            .renderer(()-> context -> new DinosaurRenderer(context,"dimorphodon", 0.5F))
            .properties(b-> b.setTrackingRange(64)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true))
            .properties(DimorphodonEntity::build)
            .attributes(DimorphodonEntity::setAttributes)
            .loot((registrateEntityLootTables, tEntityType) -> registrateEntityLootTables.add(tEntityType, noDrop()))
            .defaultSpawnEgg(5197768,16731904)
            .register();

    public static final EntityEntry<DiabloceratopsEntity> DIABLOCERATOPS = DawnEra.REGISTRATE.
            entity("diabloceratops", DiabloceratopsEntity::new, MobCategory.CREATURE)
            .renderer(() -> (context) -> new DinosaurRenderer(context, "diabloceratops", 1.0F))
            .properties(DiabloceratopsEntity::build)
            .properties((builder) -> builder.setTrackingRange(64).setUpdateInterval(2).setShouldReceiveVelocityUpdates(true))
            .attributes(DiabloceratopsEntity::createAttributes)
            .loot((lootTable, entity) -> lootTable.add(entity, noDrop()))
            .defaultSpawnEgg(5070149, 3551805).register();
}
