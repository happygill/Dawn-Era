package com.happsg.dawnera.entity.api;

import com.happsg.dawnera.entity.animals.DimorphodonEntity;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
    public List<ExtendedSensor<SmartAnimal>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>()
        );
    }



    @Override
    public BrainActivityGroup<SmartAnimal> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour(
                        new Idle<SmartAnimal>().startCondition(SmartAnimal::onGround).runFor(getRandomRuntimeProvider(50,200)).cooldownFor(getRandomRuntimeProvider(100,200)),
                        new OneRandomBehaviour(
                                new SetRandomHoverTarget<>().setRadius(20),
                                new SetRandomWalkTarget<SmartAnimal>().setRadius(16)))
        );
    }


    Function<SmartAnimal, Integer> getRandomRuntimeProvider(int start, int end){
        return (smartAnimal -> smartAnimal.getRandom().nextInt(start, end));
    }

    @Override
    public BrainActivityGroup<SmartAnimal> getCoreTasks() {
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
