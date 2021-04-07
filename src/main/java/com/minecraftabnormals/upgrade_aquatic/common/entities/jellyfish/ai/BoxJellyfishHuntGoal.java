package com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.ai;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.BoxJellyfishEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class BoxJellyfishHuntGoal extends Goal {
	private final BoxJellyfishEntity hunter;
	private int noSightTicks;

	public BoxJellyfishHuntGoal(BoxJellyfishEntity hunter) {
		this.hunter = hunter;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		LivingEntity target = this.hunter.getAttackTarget();
		if (this.hunter.isInWater() && target != null && target.isInWater() && target.isAlive() && target.getDistance(this.hunter) <= 10) {
			return true;
		} else if (target != null) {
			this.hunter.setAttackTarget(null);
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.noSightTicks < 20;
	}

	@Override
	public void startExecuting() {
		this.hunter.setHuntingCooldown();
		this.noSightTicks = 0;
	}

	@Override
	public void tick() {
		LivingEntity target = this.hunter.getAttackTarget();
		Vector3d distance = new Vector3d(target.getPosX() - this.hunter.getPosX(), target.getPosY() - this.hunter.getPosY(), target.getPosZ() - this.hunter.getPosZ());

		float pitch = -((float) (MathHelper.atan2(distance.getY(), MathHelper.sqrt(distance.getX() * distance.getX() + distance.getZ() * distance.getZ())) * (double) (180F / (float) Math.PI)));
		float yaw = (float) (MathHelper.atan2(distance.getZ(), distance.getX()) * (double) (180F / (float) Math.PI)) - 90.0F;

		this.hunter.lockedRotations[0] = MathHelper.wrapDegrees(yaw);
		this.hunter.lockedRotations[1] = MathHelper.wrapDegrees(pitch + 90.0F);

		float[] rotations = this.hunter.getRotationController().getRotations(1.0F);

		if (!this.hunter.canEntityBeSeen(target)) {
			this.noSightTicks++;
		}

		if (this.hunter.isNoEndimationPlaying() && Math.abs(rotations[0] - this.hunter.lockedRotations[0]) < 7.5F) {
			NetworkUtil.setPlayingAnimationMessage(this.hunter, BoxJellyfishEntity.SWIM_ANIMATION);
		}
	}
}