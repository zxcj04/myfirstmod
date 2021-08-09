package com.fanrende.myfirstmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class FirstBlock extends Block implements EntityBlock
{
	public FirstBlock()
	{
		super(Properties.of(Material.METAL)
				.sound(SoundType.METAL)
				.strength(2.0f)
				.lightLevel(state -> state.getValue(BlockStateProperties.POWERED) ? 14 : 0));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		return new FirstBlockTile(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
			Level level, BlockState state, BlockEntityType<T> blockEntityType
	)
	{
		if (level.isClientSide)
			return null;

		return (level1, blockPos, blockState, t) ->
		{
			if (t instanceof FirstBlockTile tile)
				tile.tickServer();
		};
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockState state = defaultBlockState().setValue(BlockStateProperties.FACING,
						context.getNearestLookingDirection().getOpposite()
				)
				.setValue(BlockStateProperties.POWERED, false);
		return state;
	}

	@Override
	public InteractionResult use(
			BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit
	)
	{
		if (!world.isClientSide)
		{
			BlockEntity tileEntity = world.getBlockEntity(pos);

			if (tileEntity instanceof MenuProvider)
			{
				NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
			}
			else
			{
				throw new IllegalStateException("Our named container provider is missing!");
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
	}
}
