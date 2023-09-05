package com.happsg.dawnera.entity.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class SmartAnimal extends PathfinderMob  {


    private static final EntityDataAccessor<Integer> DATA_ID_FOOD_LEVEL =
            SynchedEntityData.defineId(SmartAnimal.class, EntityDataSerializers.INT);

    protected SmartAnimal(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public int getFoodLevel(){
        return this.entityData.get(DATA_ID_FOOD_LEVEL);
    }
    public void increaseFoodLevel(int amount){
        this.entityData.set(DATA_ID_FOOD_LEVEL,Math.min(getFoodLevel()+amount,100));
    }
    public void decreaseFoodLevel(int amount){
        this.entityData.set(DATA_ID_FOOD_LEVEL,Math.max(getFoodLevel()-amount,0));
    }

    public abstract DietBuilder.Diet getDiet();

    protected abstract int getHungerDrainMinutes();

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("food_level", this.entityData.get(DATA_ID_FOOD_LEVEL));

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_FOOD_LEVEL, tag.getInt("food_level"));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_FOOD_LEVEL, 100);

    }

}
