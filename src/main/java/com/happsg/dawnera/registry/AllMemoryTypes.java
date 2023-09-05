package com.happsg.dawnera.registry;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

import static net.tslat.smartbrainlib.SBLForge.MEMORY_TYPES;

public class AllMemoryTypes {

    public static final Supplier<MemoryModuleType<Entity>> FOOD_TARGET = register("food_target");





    private static <T> Supplier<MemoryModuleType<T>> register(String id) {
        return register(id, null);
    }

    private static <T> Supplier<MemoryModuleType<T>> register(String id, @Nullable Codec<T> codec) {
        return registerMemoryType(id, codec);
    }

    private static <T> Supplier<MemoryModuleType<T>> registerMemoryType(String id, @org.jetbrains.annotations.Nullable Codec<T> codec) {
        return MEMORY_TYPES.register(id, () -> new MemoryModuleType<T>(Optional.ofNullable(codec)));
    }

    public static void register(IEventBus eventBus) {
        MEMORY_TYPES.register(eventBus);
    }

}
