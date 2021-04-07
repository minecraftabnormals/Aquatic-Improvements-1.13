package com.minecraftabnormals.upgrade_aquatic.common.entities.thrasher;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class SonarWaveEntity extends Entity {
	private static final DataParameter<Integer> OWNER_ID = EntityDataManager.createKey(SonarWaveEntity.class, DataSerializers.VARINT);
	private float growProgress = 0;
	private float prevGrowProgress = 0;
	
	public SonarWaveEntity(EntityType<? extends SonarWaveEntity> type, World worldIn) {
		super(type, worldIn);
		this.preventEntitySpawning = true;
	}
	
	public SonarWaveEntity(World worldIn, double x, double y, double z) {
		this(UAEntities.SONAR_WAVE.get(), worldIn);
		this.setPosition(x, y, z);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}
	
	public SonarWaveEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(UAEntities.SONAR_WAVE.get(), world);
	}

	@Override
	protected void registerData() {
		this.getDataManager().register(OWNER_ID, 0);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		this.move(MoverType.SELF, this.getMotion());
		
		if(this.getThrasherOwner() != null) {
			List<Entity> entities = this.world.getEntitiesInAABBexcluding(this.getThrasherOwner(), this.getBoundingBox().grow(this.growProgress), ThrasherEntity.ENEMY_MATCHER);
			
			for(Entity entity : entities) {
				if(entity instanceof LivingEntity && this.getThrasherOwner().getAttackTarget() == null) {
					this.getThrasherOwner().setAttackTarget((LivingEntity) entity);
				}
			}
		}
		
		Vector3d motion = this.getMotion();
		float horizontalMotionMagnitude = MathHelper.sqrt(horizontalMag(motion));
		double motionX = motion.getX();
		double motionY = motion.getY();
		double motionZ = motion.getZ();
        
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			this.rotationYaw = (float) (MathHelper.atan2(motionX, motionZ) * (double)(180F / (float)Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(motionY, horizontalMotionMagnitude) * (double)(180F / (float)Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
		
		this.rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (double)(180F / (float)Math.PI));
		
		for(this.rotationPitch = (float)(MathHelper.atan2(motionY, horizontalMotionMagnitude) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
		this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
		
		this.prevGrowProgress = this.growProgress;
		
		if(this.growProgress < 0.1F) {
			this.growProgress += 0.025F;
		} else {
			this.growProgress += 0.1F;
		}
		
		if(this.ticksExisted > 40) {
			this.remove();
		}
	}
	
	@Override
	protected void pushOutOfBlocks(double x, double y, double z) {}
	
	public void fireSonarWave(ThrasherEntity thrasher) {
		float xMotion = -MathHelper.sin(thrasher.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(thrasher.rotationPitch * ((float) Math.PI / 180F));
		float yMotion = -MathHelper.sin(thrasher.rotationPitch * ((float) Math.PI / 180F));
		float zMotion = MathHelper.cos(thrasher.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(thrasher.rotationPitch * ((float) Math.PI / 180F));
		
		Vector3d motion = new Vector3d(xMotion, yMotion, zMotion).normalize().scale(0.75D);
		
		this.setMotion(motion);
		this.setOwnerId(thrasher.getEntityId());
		this.setPosition(thrasher.getPosX() + xMotion, thrasher.getPosY(), thrasher.getPosZ() + zMotion);
		
		float motionSqrt = MathHelper.sqrt(horizontalMag(motion));
		this.rotationYaw = (float) (MathHelper.atan2(motion.x, motion.z) * (180F / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(motion.y, motionSqrt) * (180F / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}
	
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return true;
	}
	
	@Override
	public void onEnterBubbleColumn(boolean downwards) {}

	@Override
	public void onEnterBubbleColumnWithAirAbove(boolean downwards) {}
	
	@Override
	protected void doWaterSplashEffect() {}
	
	@Override
	public boolean isPushedByWater() {
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	public float getGrowProgress() {
		return MathHelper.lerp(ClientInfo.getPartialTicks(), this.prevGrowProgress, this.growProgress);
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.setOwnerId(compound.getInt("OwnerId"));
		this.growProgress = compound.getFloat("GrowProgress");
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("OwnerId", this.getOwnerId());
		compound.putFloat("GrowProgress", this.growProgress);
	}
	
	public void setOwnerId(int id) {
		this.getDataManager().set(OWNER_ID, id);
	}
	
	public int getOwnerId() {
		return this.getDataManager().get(OWNER_ID);
	}
	
	@Nullable
	public ThrasherEntity getThrasherOwner() {
		Entity owner = this.world.getEntityByID(this.getOwnerId());
		if(owner instanceof ThrasherEntity) {
			return (ThrasherEntity) owner;
		}
		return null;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}