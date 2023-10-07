package com.happsg.dawnera.entity.behaviors;

import com.happsg.dawnera.entity.api.DinosaurEntity;
import com.happsg.dawnera.registry.AllMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class FindFoodEntity<T extends DinosaurEntity> extends ExtendedBehaviour<T> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;


    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T animal) {
        Brain<?> brain = animal.getBrain();
        if(brain.checkMemory(AllMemoryTypes.FOOD_TARGET.get(),MemoryStatus.VALUE_PRESENT)){
          if(!brain.getMemory(AllMemoryTypes.FOOD_TARGET.get()).get().isRemoved())
              return false;
        }

       return !animal.getDiet().foodEntities().isEmpty();
    }

    @Override
    protected void start(T animal) {
        Brain<?> brain = animal.getBrain();
        List<Entity> entities= animal.level().getEntities(animal,animal.getBoundingBox().inflate(48));
        List<EntityType> foodEntities=animal.getDiet().foodEntities();
        Entity chosenEntity = null;
        for (Entity entity:entities) {
            if(entity instanceof LivingEntity livingEntity){
                if(foodEntities.contains(livingEntity.getType())){
                    if(chosenEntity==null||(animal.distanceTo(chosenEntity)>animal.distanceTo(livingEntity)))
                        chosenEntity=livingEntity;
                }
            }
        }
        if(chosenEntity!=null){
            BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(chosenEntity, 1, 0));
            BrainUtils.setForgettableMemory(brain, AllMemoryTypes.FOOD_TARGET.get(), chosenEntity,200);
        }
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }


    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), Pair.of(AllMemoryTypes.FOOD_TARGET.get(), MemoryStatus.REGISTERED)});
    }
}
