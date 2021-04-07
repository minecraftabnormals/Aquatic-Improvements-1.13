package com.minecraftabnormals.upgrade_aquatic.common.entities;

import com.minecraftabnormals.abnormals_core.common.entity.BucketableWaterMobEntity;
import com.minecraftabnormals.upgrade_aquatic.common.entities.thrasher.ThrasherEntity;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAEntities;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class NautilusEntity extends BucketableWaterMobEntity {
    private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(NautilusEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLEEING = EntityDataManager.createKey(NautilusEntity.class, DataSerializers.BOOLEAN);

    public NautilusEntity(EntityType<? extends NautilusEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new NautilusEntity.MoveHelperController(this);
    }

    public NautilusEntity(World world, double posX, double posY, double posZ) {
        this(UAEntities.NAUTILUS.get(), world);
        this.setPosition(posX, posY, posZ);
    }

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
    	return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 18.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.65D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 9.0F, 1.5D, 1.2D, EntityPredicates.NOT_SPECTATING::test) {

            @Override
            public void startExecuting() {
                ((NautilusEntity) this.entity).setFleeing(true);
                super.startExecuting();
            }

            @Override
            public void resetTask() {
                ((NautilusEntity) this.entity).setFleeing(false);
                super.resetTask();
            }

        });
        this.goalSelector.addGoal(2, new AvoidEntityGoal<SquidEntity>(this, SquidEntity.class, 9.0F, 1.5D, 1.2D, EntityPredicates.NOT_SPECTATING::test) {

            @Override
            public void startExecuting() {
                ((NautilusEntity) this.entity).setFleeing(true);
                super.startExecuting();
            }

            @Override
            public void resetTask() {
                ((NautilusEntity) this.entity).setFleeing(false);
                super.resetTask();
            }

        });
        this.goalSelector.addGoal(2, new AvoidEntityGoal<ThrasherEntity>(this, ThrasherEntity.class, 9.0F, 1.5D, 1.2D, EntityPredicates.NOT_SPECTATING::test) {

            @Override
            public void startExecuting() {
                ((NautilusEntity) this.entity).setFleeing(true);
                super.startExecuting();
            }

            @Override
            public void resetTask() {
                ((NautilusEntity) this.entity).setFleeing(false);
                super.resetTask();
            }

        });
        this.goalSelector.addGoal(4, new NautilusEntity.SwimGoal(this));
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new SwimmerPathNavigator(this, worldIn);
    }
    
    @Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_COD_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_TURTLE_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_TURTLE_HURT;
	}
	
	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_FISH_SWIM;
	}

    protected void registerData() {
        super.registerData();
        this.dataManager.register(MOVING, false);
        this.dataManager.register(FLEEING, false);
    }

    public boolean isFleeing() {
        return this.dataManager.get(FLEEING);
    }

    public void setFleeing(boolean p_203706_1_) {
        this.dataManager.set(FLEEING, p_203706_1_);
    }

    public boolean isMoving() {
        return this.dataManager.get(MOVING);
    }

    public void setMoving(boolean p_203706_1_) {
        this.dataManager.set(MOVING, p_203706_1_);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Moving", this.isMoving());
        compound.putBoolean("Fleeing", this.isMoving());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setMoving(compound.getBoolean("Moving"));
        this.setMoving(compound.getBoolean("Fleeing"));
    }

    public ItemStack getBucket() {
        return new ItemStack(UAItems.NAUTILUS_BUCKET.get());
    }

    @Override
    public void livingTick() {
        if (this.isMoving() && this.isInWater() && this.eyesInWater) {
            Vector3d vec3d1 = this.getLook(0.0F);

            if (this.getEntityWorld().getGameTime() % 2 == 0) {
                this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() + (this.rand.nextDouble() - 0.5D) * (double) this.getWidth() - vec3d1.x * 0.75D, this.getPosY() + this.rand.nextDouble() * (double) this.getHeight() - vec3d1.y * 1D, this.getPosZ() + (this.rand.nextDouble() - 0.5D) * (double) this.getWidth() - vec3d1.z * 0.75D, 0.0D, 0.0D, 0.0D);
            }
        }
        super.livingTick();
    }

    public void travel(Vector3d p_213352_1_) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(0.01F, p_213352_1_);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
            if (this.getAttackTarget() == null) {
                this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(p_213352_1_);
        }
    }

    @Override
    public void onEnterBubbleColumn(boolean downwards) {}

    @Override
    public void onEnterBubbleColumnWithAirAbove(boolean downwards) {}

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.65F;
    }
    
    @Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(UAItems.NAUTILUS_SPAWN_EGG.get());
	}

    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }
    
    static class MoveHelperController extends MovementController {
        private final NautilusEntity nautilus;

        MoveHelperController(NautilusEntity nautilus) {
            super(nautilus);
            this.nautilus = nautilus;
        }

        public void tick() {
            if (this.nautilus.areEyesInFluid(FluidTags.WATER)) {
                this.nautilus.setMotion(this.nautilus.getMotion().add(0.0D, 0.005D, 0.0D));
            }

            if (this.action == MovementController.Action.MOVE_TO && !this.nautilus.getNavigator().noPath()) {
                double d0 = this.posX - this.nautilus.getPosX();
                double d1 = this.posY - this.nautilus.getPosY();
                double d2 = this.posZ - this.nautilus.getPosZ();
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.nautilus.rotationYaw = this.limitAngle(this.nautilus.rotationYaw, f, 90.0F);
                this.nautilus.renderYawOffset = this.nautilus.rotationYaw;
                float f1 = (float) (this.speed * this.nautilus.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                this.nautilus.setAIMoveSpeed(MathHelper.lerp(0.125F, this.nautilus.getAIMoveSpeed(), f1));
                this.nautilus.setMotion(this.nautilus.getMotion().add(0.0D, (double)this.nautilus.getAIMoveSpeed() * d1 * 0.03D, 0.0D));
                nautilus.setMoving(true);
            } else {
            	this.nautilus.setAIMoveSpeed(0.0F);
            	nautilus.setMoving(false);
            }
        }
    }

    static class SwimGoal extends RandomSwimmingGoal {
        public final NautilusEntity nautilus;

        public SwimGoal(NautilusEntity nautilus) {
            super(nautilus, 1.0D, 30);
            this.nautilus = nautilus;
        }

        public boolean shouldExecute() {
            return super.shouldExecute();
        }
    }
}