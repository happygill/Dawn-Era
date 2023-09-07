package com.happsg.dawnera.registry;

import com.happsg.dawnera.DawnEra;
import com.happsg.dawnera.entity.animals.DimorphodonEntity;
import com.happsg.dawnera.entity.api.SmartAnimalRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

import static net.minecraft.data.loot.BlockLootSubProvider.noDrop;

public class AllEntities {
    public static void init(){}
    public static final EntityEntry<DimorphodonEntity> DIMORPHODON  = DawnEra.REGISTRATE
            .entity("dimorphodon",DimorphodonEntity::new,MobCategory.CREATURE)
            .renderer(()-> context -> new SmartAnimalRenderer(context,"dimorphodon"))
            .properties(b-> b.setTrackingRange(64)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true))
            .properties(DimorphodonEntity::build)
            .attributes(DimorphodonEntity::setAttributes)
            .loot((registrateEntityLootTables, tEntityType) -> registrateEntityLootTables.add(tEntityType, noDrop()))
            .defaultSpawnEgg(5197768,16731904)
            .register();


}
