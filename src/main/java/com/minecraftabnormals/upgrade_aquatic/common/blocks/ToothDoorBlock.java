package com.minecraftabnormals.upgrade_aquatic.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class ToothDoorBlock extends DoorBlock {

	public ToothDoorBlock(Properties builder) {
		super(builder);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (state.get(POWERED)) {
			return ActionResultType.SUCCESS;
		} else {
			state = state.func_235896_a_(OPEN);
			worldIn.setBlockState(pos, state, 10);
			worldIn.playEvent(player, state.get(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20);
			return ActionResultType.SUCCESS;
		}
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote) {
			state = state.func_235896_a_(OPEN);
			worldIn.setBlockState(pos, state, 10);
			worldIn.playEvent((PlayerEntity)null, state.get(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
		}
	}

	private int getCloseSound() {
		return this.material == Material.IRON ? 1011 : 1012;
	}

	private int getOpenSound() {
		return this.material == Material.IRON ? 1005 : 1006;
	}

}