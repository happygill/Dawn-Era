package com.happsg.dawnera.entity.animals;

import com.happsg.dawnera.entity.api.DinosaurEntity;
import com.happsg.dawnera.registry.AllAnimalDiets;
import com.happsg.dawnera.registry.AllEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class DiabloceratopsEntity extends DinosaurEntity {
    private int eatTimer = 0;
    private int eatingSoundInterval = 0;

    public DiabloceratopsEntity(EntityType<? extends DinosaurEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setDiet(AllAnimalDiets.DIABLOCERATOPS_DIET);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(Attributes.ATTACK_DAMAGE, 12.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4F);
    }

    @Override
    public void tick() {
        if (this.isEating()) {
            this.eatTimer++;
            if (this.eatTimer > 100) {
                this.eatTimer = 0;
                this.setEating(false);
            } else {
                this.eatingSoundInterval++;
                if (eatingSoundInterval > 8) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    this.eatingSoundInterval = 0;
                }
            }
        }
        super.tick();
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<DiabloceratopsEntity> entityBuilder = (EntityType.Builder<DiabloceratopsEntity>) builder;
        return entityBuilder.sized(1.8F, 1.9F);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return AllEntities.DIABLOCERATOPS.create(pLevel);
    }


    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    public static final RawAnimation EAT = RawAnimation.begin().thenPlay("eat");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller) {
        controller.add(new AnimationController<>(this, "controller", 2, (state) -> {
            if (this.isEating()) {
                state.getController().setAnimation(EAT);
                return PlayState.CONTINUE;
            } else {
                if (state.isMoving()) {
                    state.getController().setAnimation(this.isSprinting() ? RUN : WALK);
                    return PlayState.CONTINUE;
                } else if (!state.isMoving()) {
                    state.getController().setAnimation(IDLE);
                    return PlayState.CONTINUE;
                }
            }
            return PlayState.CONTINUE;
        }));
    }
}
