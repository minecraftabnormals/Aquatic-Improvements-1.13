package com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.ai;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.util.EntityUtil;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.AbstractJellyfishEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult.Type;

import java.util.EnumSet;
import java.util.Random;

public class JellyfishSwimIntoDirectionGoal extends Goal {
	private final AbstractJellyfishEntity jellyfish;
	private final Endimation swimAnimation;
	private float yaw, pitch;
	private int ticksAtRotation;

	public JellyfishSwimIntoDirectionGoal(AbstractJellyfishEntity jellyfish, Endimation swimAnimation) {
		this.jellyfish = jellyfish;
		this.swimAnimation = swimAnimation;
		this.setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		Random rand = this.jellyfish.getRNG();
		float chance = EntityUtil.rayTraceUpWithCustomDirection(this.jellyfish, this.jellyfish.lockedRotations[1], this.jellyfish.lockedRotations[0], 2.0F, 1.0F).getType() != Type.BLOCK ? 0.1F : 0.025F;
		if (this.jellyfish.isNoEndimationPlaying() && rand.nextFloat() < chance && this.jellyfish.areEyesInFluid(FluidTags.WATER)) {
			float[] generatedRotations = this.generateDirection(rand);
			this.yaw = generatedRotations[0];
			this.pitch = generatedRotations[1];

			if (EntityUtil.rayTraceUpWithCustomDirection(this.jellyfish, this.pitch, this.yaw, 1.0F, 1.0F).getType() != Type.BLOCK) {
				return !this.jellyfish.willBeBoostedOutOfWater(this.yaw, this.pitch);
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.jellyfish.isNoEndimationPlaying() && this.jellyfish.areEyesInFluid(FluidTags.WATER);
	}

	@Override
	public void startExecuting() {
		this.jellyfish.lockedRotations[0] = this.yaw;
		this.jellyfish.lockedRotations[1] = this.pitch;
	}

	@Override
	public void tick() {
		float[] jellyfishRotations = this.jellyfish.getRotationController().getRotations(1.0F);
		if (this.yaw == jellyfishRotations[0] && this.pitch == jellyfishRotations[1]) {
			this.ticksAtRotation++;
		}

		if (this.ticksAtRotation >= 5) {
			NetworkUtil.setPlayingAnimationMessage(this.jellyfish, this.swimAnimation);
		}
	}

	@Override
	public void resetTask() {
		this.ticksAtRotation = 0;
	}

	private float[] generateDirection(Random rand) {
		float[] rotations = this.jellyfish.getRotationController().getRotations(1.0F);
		float upperChance = this.jellyfish.getPositionVec().getY() < this.jellyfish.world.getSeaLevel() - 6 ? 0.5F : 0.2F;
		if (this.jellyfish.isOnGround() || rotations[0] == 0.0F && rotations[1] == 0.0F || EntityUtil.rayTraceUpWithCustomDirection(this.jellyfish, rotations[1], rotations[0], 1.0F, 1.0F).getType() == Type.BLOCK) {
			return new float[]{
					(float) MathHelper.wrapDegrees(MathUtil.makeNegativeRandomly(rand.nextFloat() * 180.0F, rand)),
					(float) MathHelper.wrapDegrees(MathUtil.makeNegativeRandomly(rand.nextFloat() * 180.0F, rand))
			};
		}
		return new float[]{
				rotations[0] + (float) MathHelper.wrapDegrees(MathUtil.makeNegativeRandomly(rand.nextFloat() * 100.0F, rand)),
				rand.nextFloat() < upperChance ? (float) MathUtil.makeNegativeRandomly(rand.nextFloat() * 50.0F, rand) : rotations[1] + (float) MathHelper.wrapDegrees(MathUtil.makeNegativeRandomly(rand.nextFloat() * 100.0F, rand))
		};
	}
}