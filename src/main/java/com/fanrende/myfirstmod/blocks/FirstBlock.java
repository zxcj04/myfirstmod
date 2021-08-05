package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class FirstBlock extends Block
{
	public FirstBlock()
	{
		super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f).lightValue(14));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public int getLightValue(BlockState state)
	{
		return state.get(BlockStateProperties.POWERED) ? super.getLightValue(state) : 0;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new FirstBlockTile();
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = getDefaultState().with(
				BlockStateProperties.FACING,
				context.getNearestLookingDirection().getOpposite()
		).with(BlockStateProperties.POWERED, false);
		return state;
	}

	@Override
	public ActionResultType onBlockActivated(
			BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit
	)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof INamedContainerProvider)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player,
						(INamedContainerProvider) tileEntity,
						tileEntity.getPos()
				);
			}
			else
			{
				throw new IllegalStateException("Our named container provider is missing!");
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
	}
}
