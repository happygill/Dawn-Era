package com.happsg.dawnera.entity.api;

import com.happsg.dawnera.entity.behaviors.EatFood;
import com.happsg.dawnera.entity.behaviors.FindFoodEntity;
import com.happsg.dawnera.entity.behaviors.FindFoodItem;
import com.happsg.dawnera.entity.behaviors.HungerDrain;
import com.happsg.dawnera.registry.AllAnimalDiets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import com.happsg.dawnera.entity.api.DietBuilder.Diet;

import java.util.List;
import java.util.function.Function;

public abstract class DinosaurEntity extends Animal implements GeoEntity, SmartBrainOwner<DinosaurEntity> {
    final AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);

    Diet diet= AllAnimalDiets.EMPTY_DIET;

    private static final EntityDataAccessor<Integer> DATA_ID_FOOD_LEVEL =
            SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_ID_MALE_GENDER =
            SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_EATING =
            SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);

    // Not used for anything, yet
    private static final EntityDataAccessor<Boolean> DATA_SLEEPING =
            SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);

    protected DinosaurEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
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

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        if(diet!=null)
            this.diet = diet;
    }

    public boolean isEating() {
        return this.entityData.get(DATA_EATING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(DATA_EATING, eating);
    }

    public boolean isSleeping() {
        return this.entityData.get(DATA_SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(DATA_SLEEPING, sleeping);
    }

    @Override
    public List<ExtendedSensor<DinosaurEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<DinosaurEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour(
                        new Idle<DinosaurEntity>().startCondition(DinosaurEntity::onGround).runFor(getRandomRuntimeProvider(50,200)).cooldownFor(getRandomRuntimeProvider(100,200)),
                        new OneRandomBehaviour(
                                new SetRandomHoverTarget<>().setRadius(20),
                                new SetRandomWalkTarget<DinosaurEntity>().setRadius(16)))
        );
    }


    Function<DinosaurEntity, Integer> getRandomRuntimeProvider(int start, int end){
        return (dinosaur -> dinosaur.getRandom().nextInt(start, end));
    }

    @Override
    public BrainActivityGroup<DinosaurEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new HungerDrain<>(),
                new FindFoodItem<>(),
                new FindFoodEntity<>(),
                new EatFood<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    protected void registerGoals() {}

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("food_level", this.entityData.get(DATA_ID_FOOD_LEVEL));
        tag.putBoolean("male_gender", this.entityData.get(DATA_ID_MALE_GENDER));
        tag.putBoolean("eating", this.entityData.get(DATA_EATING));
        tag.putBoolean("sleeping", this.entityData.get(DATA_SLEEPING));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_FOOD_LEVEL, tag.getInt("food_level"));
        this.entityData.set(DATA_ID_MALE_GENDER, tag.getBoolean("male_gender"));
        this.entityData.set(DATA_EATING, tag.getBoolean("eating"));
        this.entityData.set(DATA_SLEEPING, tag.getBoolean("sleeping"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_FOOD_LEVEL, 100);
        this.entityData.define(DATA_ID_MALE_GENDER, Boolean.TRUE);
        this.entityData.define(DATA_EATING, false);
        this.entityData.define(DATA_SLEEPING, false);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEating() || this.isSleeping()) {
            this.navigation.stop();
            this.setDeltaMovement(Vec3.ZERO);
        }
        super.travel(pTravelVector);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.entityData.set(DATA_ID_MALE_GENDER, this.random.nextBoolean());
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
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
