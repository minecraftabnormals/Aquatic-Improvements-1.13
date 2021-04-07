package com.minecraftabnormals.upgrade_aquatic.common.blocks;

import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks.KelpType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.KelpTopBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class UAKelpTopBlock extends KelpTopBlock {
	private KelpType kelpType;
	
	public UAKelpTopBlock(KelpType kelpType, Properties props) {
		super(props);
		this.kelpType = kelpType;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if(!state.isValidPosition(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		} else {
			BlockPos blockpos = pos.up();
			BlockState blockstate = worldIn.getBlockState(blockpos);
			if(blockstate.getBlock() == Blocks.WATER && state.get(AGE) < 25 && random.nextDouble() < this.kelpType.getGrowChance()) {
				worldIn.setBlockState(blockpos, state.func_235896_a_(AGE));
			}
		}
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		Block block = blockstate.getBlock();
		if(block == Blocks.MAGMA_BLOCK) {
			return false;
		} else {
			return block == this || block == this.getPlantBlock() || blockstate.isSolidSide(worldIn, blockpos, Direction.UP);
		}
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if(!stateIn.isValidPosition(worldIn, currentPos)) {
			if(facing == Direction.DOWN) {
				return Blocks.AIR.getDefaultState();
			}
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		if(facing == Direction.UP && facingState.getBlock() == this) {
			return this.getPlantBlock().getDefaultState();
		} else {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			return stateIn;
		}
	}
	
	public Block getPlantBlock() {
		switch(this.kelpType) {
			default:
			case TONGUE:
				return UABlocks.TONGUE_KELP_PLANT.get();
			case THORNY:
				return UABlocks.THORNY_KELP_PLANT.get();
			case OCHRE:
				return UABlocks.OCHRE_KELP_PLANT.get();
			case POLAR:
				return UABlocks.POLAR_KELP_PLANT.get();
		}
	}

}
