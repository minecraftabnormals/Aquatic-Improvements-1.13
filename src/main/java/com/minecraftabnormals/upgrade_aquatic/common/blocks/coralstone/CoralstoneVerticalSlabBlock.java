package com.minecraftabnormals.upgrade_aquatic.common.blocks.coralstone;

import com.minecraftabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class CoralstoneVerticalSlabBlock extends VerticalSlabBlock {
	@Nullable
	private Block[] growableCoralBlocks;
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	
	public CoralstoneVerticalSlabBlock(Properties properties, @Nullable Block[] growableCoralBlocks) {
		super(properties);
		this.growableCoralBlocks = growableCoralBlocks;
		this.setDefaultState(this.getDefaultState()
			.with(TYPE, VerticalSlabType.NORTH)
			.with(WATERLOGGED, false)
			.with(POWERED, false)
		);
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if(!worldIn.isAreaLoaded(pos, 3)) return;
		Block block = state.getBlock();
		VerticalSlabType type = state.get(TYPE);
		
		if(this.growableCoralBlocks == null && block != UABlocks.DEAD_CORALSTONE_SLAB.get()) {
			for(int i = 0; i < 4; i++) {
				BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
				UABlocks.CORALSTONE_VERTICAL_SLAB_CONVERSION_MAP.forEach((input, output) -> {
				    if(input.get() == worldIn.getBlockState(blockpos).getBlock()) {
						BlockState newState = output.get().getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED));
				    	worldIn.setBlockState(pos, newState, 2);
				    }
				});
			}
		}
		
		if(this.growableCoralBlocks != null && random.nextFloat() < 0.12F && state.get(POWERED)) {
			Direction randDirection = this.growableCoralBlocks.length > 3 ? Direction.getRandomDirection(random) : Direction.byIndex(random.nextInt(5) + 1);
			BlockPos growPos = type.direction != null ? pos.offset(type.direction.getOpposite()) : pos.offset(randDirection);
			FluidState fluidState = worldIn.getBlockState(growPos).getFluidState();
			boolean validGrowPos = this.isValidPosToGrow(worldIn, growPos, fluidState);
			
			if(state.get(TYPE) != VerticalSlabType.DOUBLE) {
				if(validGrowPos) {
					worldIn.setBlockState(growPos, this.growableCoralBlocks[2].getDefaultState().with(CoralWallFanBlock.FACING, type.direction.getOpposite()), 2);
				}
			} else {
				if(validGrowPos) {
					if(randDirection.getIndex() > 1) {
						worldIn.setBlockState(growPos, this.growableCoralBlocks[2].getDefaultState().with(CoralWallFanBlock.FACING, randDirection), 2);
					} else if(randDirection.getIndex() == 1) {
						if(random.nextBoolean()) {
							worldIn.setBlockState(growPos, this.growableCoralBlocks[1].getDefaultState(), 2);
						} else {
							worldIn.setBlockState(growPos, this.growableCoralBlocks[0].getDefaultState(), 2);
						}
					} else {
						worldIn.setBlockState(growPos, this.growableCoralBlocks[3].getDefaultState(), 2);
					}
				}
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(!worldIn.isRemote) {
			boolean flag = state.get(POWERED);
			if(flag != worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.func_235896_a_(POWERED), 2);
			}
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).with(POWERED, context.getWorld().isBlockPowered(context.getPos()));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() == Items.SHEARS && state.getBlock() != UABlocks.CORALSTONE_VERTICAL_SLAB.get()) {
			BlockState newState = UABlocks.CORALSTONE_VERTICAL_SLAB.get().getDefaultState();
			world.playSound(null, pos, SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.PLAYERS, 1.0F, 0.8F);
			stack.damageItem(1, player, (entity) -> entity.sendBreakAnimation(hand));
			world.setBlockState(pos, newState.with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED)), 2);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, POWERED);
	}
	
	private boolean isValidPosToGrow(World world, BlockPos pos, FluidState fluidState) {
		return world.getBlockState(pos).getMaterial().isReplaceable() && fluidState.getLevel() >= 8 && fluidState.isTagged(FluidTags.WATER);
	}
}
