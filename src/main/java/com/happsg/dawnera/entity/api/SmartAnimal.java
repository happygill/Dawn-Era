package com.happsg.dawnera.entity.api;

import com.happsg.dawnera.registry.AllAnimalDiets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import com.happsg.dawnera.entity.api.DietBuilder.Diet;


public abstract class SmartAnimal extends PathfinderMob implements GeoEntity, SmartBrainOwner<SmartAnimal> {

    final AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);



    Diet diet= AllAnimalDiets.EMPTY_DIET;

    private static final EntityDataAccessor<Integer> DATA_ID_FOOD_LEVEL =
            SynchedEntityData.defineId(SmartAnimal.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_ID_MALE_GENDER =
            SynchedEntityData.defineId(SmartAnimal.class, EntityDataSerializers.BOOLEAN);

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

    public boolean isMale() {
        return entityData.get(DATA_ID_MALE_GENDER);
    }
    public boolean isFemale() {
        return !isMale();
    }
    public Diet getDiet() { return diet; }
    public void setDiet(Diet diet) {
        if(diet!=null)
            this.diet = diet;
    }


    @Override
    protected void registerGoals() {}

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("food_level", this.entityData.get(DATA_ID_FOOD_LEVEL));
        tag.putBoolean("male_gender", this.entityData.get(DATA_ID_MALE_GENDER));

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_FOOD_LEVEL, tag.getInt("food_level"));
        this.entityData.set(DATA_ID_MALE_GENDER, tag.getBoolean("male_gender"));


    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_FOOD_LEVEL, 100);
        this.entityData.define(DATA_ID_MALE_GENDER, Boolean.TRUE);

    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.entityData.set(DATA_ID_MALE_GENDER, this.random.nextBoolean());
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

}
