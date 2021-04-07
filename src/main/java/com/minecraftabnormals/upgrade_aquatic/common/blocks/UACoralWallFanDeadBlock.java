package com.minecraftabnormals.upgrade_aquatic.common.blocks;

import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class UACoralWallFanDeadBlock extends DeadCoralWallFanBlock {

	public UACoralWallFanDeadBlock(Properties props) {
		super(props);
	}
	
	@Override
	public boolean isConduitFrame(BlockState state, IWorldReader world, BlockPos pos, BlockPos conduit) {
		return state.getBlock() == UABlocks.ELDER_PRISMARINE_CORAL_WALL_FAN.get();
	}

}