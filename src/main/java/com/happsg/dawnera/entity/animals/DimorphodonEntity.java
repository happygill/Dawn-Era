package com.happsg.dawnera.entity.animals;

import com.happsg.dawnera.entity.api.DietBuilder;
import com.happsg.dawnera.entity.api.SmartAnimal;
import com.happsg.dawnera.entity.behaviors.EatFood;
import com.happsg.dawnera.entity.behaviors.FindFoodEntity;
import com.happsg.dawnera.entity.behaviors.FindFoodItem;
import com.happsg.dawnera.registry.AllAnimalDiets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomHoverTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Function;

public class DimorphodonEntity extends SmartAnimal{



    private final static RawAnimation FLY_ANIMATION=RawAnimation.begin().then("animation.dimorphodon.fly", Animation.LoopType.LOOP);
    private final static RawAnimation IDLE_ANIMATION=RawAnimation.begin().then("animation.dimorphodon.idle", Animation.LoopType.LOOP);



    public DimorphodonEntity(EntityType<? extends SmartAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, false);

    }

    @Override
    public DietBuilder.Diet getDiet() {
        return AllAnimalDiets.DIMORPHODAN_DIET;
    }

    @Override
    protected int getHungerDrainMinutes() {
        return 48;
    }



    @Override
    public List<ExtendedSensor<DimorphodonEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<SmartAnimal> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FindFoodItem<>(),
                new FindFoodEntity<>(),
                new EatFood<>(),
                new MoveToWalkTarget()
        );
    }

    @Override
    public BrainActivityGroup<SmartAnimal> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour(
                        new Idle<DimorphodonEntity>().startCondition(DimorphodonEntity::onGround).runFor(getRandomRuntimeProvider(50,200)).cooldownFor(getRandomRuntimeProvider(100,200)),
                        new OneRandomBehaviour(
                                new SetRandomHoverTarget<>().setRadius(20),
                                new SetRandomWalkTarget<DimorphodonEntity>().setRadius(16)))
        );
    }

    Function<DimorphodonEntity, Integer> getRandomRuntimeProvider(int start,int end){
        return (dimorphodonEntity -> dimorphodonEntity.getRandom().nextInt(start, end));
    }



    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.FLYING_SPEED, 0.15F)
                .add(Attributes.MOVEMENT_SPEED, .15F)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                this.moveRelative(this.getSpeed(), pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
            }
        }
        this.calculateEntityAnimation(false);
    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<DimorphodonEntity> entityBuilder = (EntityType.Builder<DimorphodonEntity>) builder;
        return entityBuilder.sized(1f, 1f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller",
                0, this::predicate));
    }



    private PlayState predicate(AnimationState<DimorphodonEntity> animationState) {
        if (animationState.isMoving()) {
            animationState.getController().setAnimation(FLY_ANIMATION);
            return PlayState.CONTINUE;
        }
        animationState.getController().setAnimation(IDLE_ANIMATION);
        return PlayState.CONTINUE;

    }

    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }
}
