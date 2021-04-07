package com.minecraftabnormals.upgrade_aquatic.common.entities;

import com.google.common.collect.Lists;
import com.minecraftabnormals.upgrade_aquatic.common.entities.goal.PerchHideInSeagrassGoal;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UAItems;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UASounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class PerchEntity extends AbstractGroupFishEntity {
	public int hideCooldown;

	public PerchEntity(EntityType<? extends PerchEntity> type, World world) {
		super(type, world);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new PerchHideInSeagrassGoal(this));
	}

	public boolean isSeagrassNearby() {
		return !this.getNearbySeagrass().isEmpty();
	}

	public List<BlockPos> getNearbySeagrass() {
		List<BlockPos> seagrasses = Lists.newArrayList();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = this.getPosition().getY() - 6; yy <= this.getPosY() + 6; yy++) {
			for (int xx = this.getPosition().getX() - 12; xx <= this.getPosX() + 12; xx++) {
				for (int zz = this.getPosition().getZ() - 12; zz <= this.getPosZ() + 12; zz++) {
					mutable.setPos(xx, yy, zz);
					BlockState block = this.world.getBlockState(mutable);
					if (block.isIn(Blocks.SEAGRASS) || block.isIn(Blocks.TALL_SEAGRASS)) {
						seagrasses.add(mutable);
					}
				}
			}
		}
		return seagrasses;
	}

	public ItemStack getFishBucket() {
		return new ItemStack(UAItems.PERCH_BUCKET.get());
	}

	protected SoundEvent getAmbientSound() {
		return UASounds.ENTITY_PERCH_AMBIENT.get();
	}

	protected SoundEvent getDeathSound() {
		return UASounds.ENTITY_PERCH_DEATH.get();
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return UASounds.ENTITY_PERCH_HURT.get();
	}

	protected SoundEvent getFlopSound() {
		return UASounds.ENTITY_PERCH_FLOP.get();
	}
}