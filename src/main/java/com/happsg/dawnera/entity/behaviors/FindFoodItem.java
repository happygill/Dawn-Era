package com.happsg.dawnera.entity.behaviors;

import com.happsg.dawnera.entity.api.SmartAnimal;
import com.happsg.dawnera.registry.AllMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class FindFoodItem<T extends SmartAnimal> extends ExtendedBehaviour<T> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;


    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T entity) {
       return !entity.getDiet().foodItems().isEmpty();
    }

    @Override
    protected void start(T animal) {
        Brain<?> brain = animal.getBrain();
        List<Entity> entities= animal.level().getEntities(animal,animal.getBoundingBox().inflate(48));
        List<Item> foodItems=animal.getDiet().foodItems();
        ItemEntity chosenItem = null;
        for (Entity entity:entities) {
            if(entity instanceof ItemEntity item){
                if(foodItems.contains(item.getItem().getItem())){
                    if(chosenItem==null||(animal.distanceTo(chosenItem)>entity.distanceTo(item)))
                        chosenItem=item;
                }
            }
        }
        if(chosenItem!=null){
            BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(chosenItem, 1, 0));
            BrainUtils.setForgettableMemory(brain, AllMemoryTypes.FOOD_TARGET.get(), chosenItem,600);
        }
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }


    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), Pair.of(AllMemoryTypes.FOOD_TARGET.get(), MemoryStatus.VALUE_ABSENT)});
    }
}
