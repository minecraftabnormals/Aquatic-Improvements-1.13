package com.minecraftabnormals.upgrade_aquatic.common.entities;

import com.minecraftabnormals.abnormals_core.common.entity.BucketableWaterMobEntity;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAEntities;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Predicate;

public class LionfishEntity extends BucketableWaterMobEntity {
	private static final Predicate<LivingEntity> ENEMY_MATCHER = (entity) -> {
		if (entity == null) {
			return false;
		} else {
			return !(entity instanceof LionfishEntity) && !(entity instanceof AbstractFishEntity);
		}
	};
	private static final DataParameter<Boolean> HUNGY = EntityDataManager.createKey(LionfishEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TIME_TILL_HUNGRY = EntityDataManager.createKey(LionfishEntity.class, DataSerializers.VARINT);
	int lastTimeSinceHungry;

	public LionfishEntity(EntityType<? extends LionfishEntity> type, World world) {
		super(UAEntities.LIONFISH.get(), world);
		this.moveController = new LionfishEntity.MoveHelperController(this);
	}
	
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
    	return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 8.0D);
    }
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.35D, 30) {
			
			@Override
			public boolean shouldExecute() {
				if (this.creature.isBeingRidden()) {
					return false;
				} else {
					if (!this.mustUpdate) {
						if (this.creature.getIdleTime() >= 100) {
							return false;
						}
						if(((LionfishEntity)this.creature).isHungry()) {
							if (this.creature.getRNG().nextInt(60) != 0) {
								return false;
							}
						} else {
							if (this.creature.getRNG().nextInt(30) != 0) {
								return false;
							}
						}
					}

					Vector3d vec3d = this.getPosition();
					if (vec3d == null) {
						return false;
					} else {
						this.x = vec3d.x;
						this.y = vec3d.y;
						this.z = vec3d.z;
						this.mustUpdate = false;
						return true;
					}
				}
			}
			
		});
		this.goalSelector.addGoal(4, new LionfishEntity.LionfishAttackGoal(this, 12D, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<TropicalFishEntity>(this, TropicalFishEntity.class, true) {
			
			@Override
			public boolean shouldExecute() {
				return ((LionfishEntity)this.goalOwner).isHungry() && super.shouldExecute();
			}
			
		});
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}
	
	protected void registerData() {
		super.registerData();
		this.dataManager.register(HUNGY, true);
		this.dataManager.register(TIME_TILL_HUNGRY, 0);
    }

    public boolean isHungry() {
        return this.dataManager.get(HUNGY);
    }

    public void setHungry(boolean hungry) {
        this.dataManager.set(HUNGY, hungry);
    }
    
    public int getTimeTillHungry() {
        return this.dataManager.get(TIME_TILL_HUNGRY);
    }

    public void setTimeTillHungry(int ticks) {
        this.dataManager.set(TIME_TILL_HUNGRY, ticks);
    }
    
    public void writeAdditional(CompoundNBT compound) {
    	super.writeAdditional(compound);
    	compound.putBoolean("IsHungry", this.isHungry());
    	compound.putInt("TimeTillHungry", this.getTimeTillHungry());
    }

    public void readAdditional(CompoundNBT compound) {
    	super.readAdditional(compound);
    	this.setHungry(compound.getBoolean("IsHungry"));
    	this.setTimeTillHungry(compound.getInt("TimeTillHungry"));
    }
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.85F;
	}

    @Override
    public int getMaxSpawnedInChunk() {
        return 3;
    }
    
    @Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(UAItems.LIONFISH_SPAWN_EGG.get());
	}

	@Override
	public ItemStack getBucket() {
		return new ItemStack(UAItems.LIONFISH_BUCKET.get());
	}
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		return new SwimmerPathNavigator(this, worldIn);
	}

	public static boolean coralCondition(EntityType<? extends Entity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if (((World) world).getDimensionKey() != World.OVERWORLD) return false;
		for (int yy = pos.getY() - 2; yy <= pos.getY() + 2; yy++) {
			for (int xx = pos.getX() - 6; xx <= pos.getX() + 6; xx++) {
				for (int zz = pos.getZ() - 6; zz <= pos.getZ() + 6; zz++) {
					if (world.getBlockState(new BlockPos(xx, yy, zz)).getBlock().isIn(BlockTags.CORAL_BLOCKS)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isInWater() && this.onGround && this.collidedVertically) {
			this.setMotion(this.getMotion().add((double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.035F), (double)0.4F, (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.035F)));
			this.onGround = false;
			this.isAirBorne = true;
			this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
		}
		if (this.isAlive()) {
			for(LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(0.3D), ENEMY_MATCHER)) {
				if (entity.isAlive()) {
					this.attack(entity);
				}
			}
		}
		if(!this.isHungry() && lastTimeSinceHungry < this.getTimeTillHungry()) {
			lastTimeSinceHungry++;
		}
		if(lastTimeSinceHungry >= this.getTimeTillHungry()) {
			this.setHungry(true);
			lastTimeSinceHungry = 0;
		}
	}
	
	private void attack(LivingEntity entity) {
		if(entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2.0F) && entity.isInWater()) {
			entity.addPotionEffect(new EffectInstance(Effects.POISON, 70, 1));
			this.playSound(SoundEvents.ENTITY_PUFFER_FISH_STING, 1.0F, 1.0F);
			if(entity instanceof PlayerEntity) {
				this.setAttackTarget(entity);
			}
		}
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount) {
		Entity entitySource = source.getTrueSource();
		if(entitySource instanceof LivingEntity && !(entitySource instanceof PlayerEntity && ((PlayerEntity) entitySource).abilities.isCreativeMode)) {
			if(entitySource instanceof PlayerEntity) {
				this.setAttackTarget((LivingEntity) entitySource);
			}
			return super.attackEntityFrom(source, amount);
		} else {
			return super.attackEntityFrom(source, amount);
		}
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
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PUFFER_FISH_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PUFFER_FISH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_PUFFER_FISH_HURT;
	}
	
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_PUFFER_FISH_FLOP;
	}
	
	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_FISH_SWIM;
	}
	
	static class MoveHelperController extends MovementController {
        private final LionfishEntity lionfish;

        MoveHelperController(LionfishEntity lionfish) {
            super(lionfish);
            this.lionfish = lionfish;
        }

        public void tick() {
            if (this.lionfish.areEyesInFluid(FluidTags.WATER)) {
                this.lionfish.setMotion(this.lionfish.getMotion().add(0.0D, 0.005D, 0.0D));
            }

            if (this.action == MovementController.Action.MOVE_TO && !this.lionfish.getNavigator().noPath()) {
            	double d0 = this.posX - this.lionfish.getPosX();
            	double d1 = this.posY - this.lionfish.getPosY();
            	double d2 = this.posZ - this.lionfish.getPosZ();
            	double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            	d1 = d1 / d3;
            	float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
            	this.lionfish.rotationYaw = this.limitAngle(this.lionfish.rotationYaw, f, 90.0F);
            	this.lionfish.renderYawOffset = this.lionfish.rotationYaw;
            	float f1 = (float) (this.speed * this.lionfish.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
            	this.lionfish.setAIMoveSpeed(MathHelper.lerp(0.125F, this.lionfish.getAIMoveSpeed(), f1));
            	this.lionfish.setMotion(this.lionfish.getMotion().add(0.0D, (double)this.lionfish.getAIMoveSpeed() * d1 * 0.03D, 0.0D));
            }
        }
    }
	
	static class LionfishAttackGoal extends MeleeAttackGoal {
		
		public LionfishAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
			super(creature, speedIn, useLongMemory);
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return super.shouldContinueExecuting() && attacker.isInWater();
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
			double d0 = this.getAttackReachSqr(enemy);
			if (distToEnemySqr <= d0 && this.func_234041_j_() <= 0) {
				this.func_234039_g_();
				((LionfishEntity)this.attacker).attack(enemy);
				((LionfishEntity)this.attacker).setHungry(false);
				((LionfishEntity)this.attacker).setTimeTillHungry(attacker.getRNG().nextInt(300) + 300);
				if(enemy instanceof PlayerEntity) {
					attacker.setAttackTarget(null);
					this.resetTask();
				}
			}
		}
		
	}

}