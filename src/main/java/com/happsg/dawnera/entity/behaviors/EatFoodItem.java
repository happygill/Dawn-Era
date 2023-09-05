package com.happsg.dawnera.entity.behaviors;

import com.happsg.dawnera.entity.api.SmartAnimal;
import com.happsg.dawnera.registry.AllMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class EatFoodItem<T extends SmartAnimal> extends ExtendedBehaviour<T> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T animal) {
        Brain<?> brain = animal.getBrain();

        if(brain.getMemory(AllMemoryTypes.FOOD_TARGET.get()).get() instanceof ItemEntity item) {
            return animal.distanceTo(item)<1;
        }
        return false;
    }

    @Override
    protected void start(T animal) {
        Brain<?> brain = animal.getBrain();
        if(brain.getMemory(AllMemoryTypes.FOOD_TARGET.get()).get() instanceof ItemEntity item) {
            item.discard();
            brain.eraseMemory(AllMemoryTypes.FOOD_TARGET.get());
        }
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }


    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(AllMemoryTypes.FOOD_TARGET.get(), MemoryStatus.VALUE_PRESENT)});
    }
}
