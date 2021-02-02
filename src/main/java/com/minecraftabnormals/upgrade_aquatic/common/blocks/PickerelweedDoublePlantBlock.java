package com.minecraftabnormals.upgrade_aquatic.common.blocks;

import com.minecraftabnormals.upgrade_aquatic.common.entities.pike.PikeEntity;
import com.minecraftabnormals.upgrade_aquatic.core.registry.UABlocks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class PickerelweedDoublePlantBlock extends Block implements IGrowable, IWaterLoggable {
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty FAKE_WATERLOGGED = BooleanProperty.create("fake_waterlogged");

	public PickerelweedDoublePlantBlock(Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HALF, DoubleBlockHalf.LOWER).with(FAKE_WATERLOGGED, false));
	}

	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return this == UABlocks.TALL_BLUE_PICKERELWEED.get() ? new ItemStack(UABlocks.BLUE_PICKERELWEED.get()) : new ItemStack(UABlocks.PURPLE_PICKERELWEED.get());
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (!(entity instanceof PikeEntity)) {
			entity.setMotionMultiplier(state, new Vector3d(0.75D, 0.75D, 0.75D));
		}
	}

	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.CLAY;
	}

	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		DoubleBlockHalf doubleblockhalf = stateIn.get(HALF);
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			if (doubleblockhalf == DoubleBlockHalf.UPPER && !stateIn.get(FAKE_WATERLOGGED)) {
				stateIn = stateIn.with(FAKE_WATERLOGGED, true);
				if (worldIn.getBlockState(currentPos.down()).hasProperty(FAKE_WATERLOGGED))
					worldIn.setBlockState(currentPos.down(), worldIn.getBlockState(currentPos.down()).with(FAKE_WATERLOGGED, true), 2);
			} else if (doubleblockhalf == DoubleBlockHalf.LOWER && !stateIn.get(FAKE_WATERLOGGED)) {
				stateIn = stateIn.with(FAKE_WATERLOGGED, true);
				if (worldIn.getBlockState(currentPos.up()).hasProperty(FAKE_WATERLOGGED))
					worldIn.setBlockState(currentPos.up(), worldIn.getBlockState(currentPos.up()).with(FAKE_WATERLOGGED, true), 2);
			}
		}
		if (facing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.isIn(this) && facingState.get(HALF) != doubleblockhalf) {
			return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		BlockPos blockpos = context.getPos();
		return blockpos.getY() < context.getWorld().getHeight() - 1 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? super.getStateForPlacement(context).with(WATERLOGGED, ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8).with(FAKE_WATERLOGGED, ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8) : null;
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		if (state.get(HALF) != DoubleBlockHalf.UPPER) {
			return this.isValidGround(worldIn.getBlockState(pos.down()), worldIn, pos);
		} else {
			BlockState blockstate = worldIn.getBlockState(pos.down());
			if (state.getBlock() != this)
				this.isValidGround(worldIn.getBlockState(pos.down()), worldIn, pos);
			return blockstate.isIn(this) && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	public void placeAt(IWorld p_196390_1_, BlockPos p_196390_2_, int flags) {
		FluidState ifluidstate = p_196390_1_.getFluidState(p_196390_2_);
		FluidState ifluidstateUp = p_196390_1_.getFluidState(p_196390_2_.up());
		boolean applyFakeWaterLogging = ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8 || ifluidstateUp.isTagged(FluidTags.WATER) && ifluidstateUp.getLevel() == 8;
		p_196390_1_.setBlockState(p_196390_2_, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(WATERLOGGED, ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8).with(FAKE_WATERLOGGED, applyFakeWaterLogging), flags);
		p_196390_1_.setBlockState(p_196390_2_.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(WATERLOGGED, ifluidstateUp.isTagged(FluidTags.WATER) && ifluidstateUp.getLevel() == 8).with(FAKE_WATERLOGGED, applyFakeWaterLogging), flags);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED, FAKE_WATERLOGGED);
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return state.get(WATERLOGGED) ? 0 : 60;
	}

	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XZ;
	}

	@OnlyIn(Dist.CLIENT)
	public long getPositionRandom(BlockState state, BlockPos pos) {
		return MathHelper.getCoordinateRandom(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		if (!worldIn.isRemote) {
			cont:
			for (int i = 0; i < 128; ++i) {
				BlockPos blockpos = pos;
				BlockState blockstate = this == UABlocks.TALL_BLUE_PICKERELWEED.get() ? UABlocks.BLUE_PICKERELWEED.get().getDefaultState() : UABlocks.PURPLE_PICKERELWEED.get().getDefaultState();

				for (int j = 0; j < i / 16; ++j) {
					blockpos = blockpos.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
					if (Block.isOpaque(worldIn.getBlockState(blockpos).getCollisionShape(worldIn, blockpos))) {
						continue cont;
					}
				}

				if (blockstate.isValidPosition(worldIn, blockpos) && worldIn.getBlockState(blockpos).getMaterial().isReplaceable() && rand.nextFloat() <= 0.10F) {
					BlockState blockstate1 = worldIn.getBlockState(blockpos);
					if (blockstate1.getFluidState().isTagged(FluidTags.WATER) && worldIn.getFluidState(blockpos).getLevel() == 8) {
						worldIn.setBlockState(blockpos, blockstate.with(WATERLOGGED, true), 3);
					} else {
						worldIn.setBlockState(blockpos, blockstate.with(WATERLOGGED, false), 3);
					}
				}
			}
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote) {
			if (player.abilities.isCreativeMode) {
				removeBottomHalf(worldIn, pos, state, player);
			} else {
				spawnDrops(state, worldIn, pos, null, player, player.getHeldItemMainhand());
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}

	protected static void removeBottomHalf(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		if (doubleblockhalf == DoubleBlockHalf.UPPER) {
			BlockPos blockpos = pos.down();
			BlockState blockstate = world.getBlockState(blockpos);
			if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
				world.setBlockState(blockpos, world.getFluidState(blockpos).getLevel() == 8 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState(), 51);
				world.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			}
		} else if (doubleblockhalf == DoubleBlockHalf.LOWER) {
			BlockPos blockpos = pos.up();
			BlockState blockstate = world.getBlockState(blockpos);
			if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.UPPER) {
				world.setBlockState(blockpos, world.getFluidState(blockpos).getLevel() == 8 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState(), 51);
				world.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			}
		}
	}
}