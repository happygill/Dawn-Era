package com.happsg.dawnera.entity.behaviors;

import com.happsg.dawnera.entity.api.DinosaurEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class HungerDrain <T extends DinosaurEntity> extends ExtendedBehaviour<T> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of();

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T animal) {
        return animal.getDiet().hasHunger()&&animal.getFoodLevel()>0;
    }

    @Override
    protected void start(T animal) {
        animal.decreaseFoodLevel(1);
    }

    public HungerDrain() {
        cooldownFor(entity -> entity.getDiet().hungerDrainMinutes()*20*60/100);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }
}
