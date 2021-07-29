package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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
		setRegistryName("firstblock");
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new FirstBlockTile();
	}

	@Override
	public void onBlockPlacedBy(
			World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack
	)
	{
		if (placer != null)
		{
			world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2);
		}
	}


	@Override
	public boolean onBlockActivated(
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

				return true;
			}
			else
			{
				throw new IllegalStateException("Our named container provider is missing!");
			}
		}

		return super.onBlockActivated(state, world, pos, player, handIn, hit);
	}

	private static Direction getFacingFromEntity(BlockPos clickedPos, LivingEntity entity)
	{
		return Direction.getFacingFromVector((float) ( entity.posX - clickedPos.getX() ),
				(float) ( entity.posY - clickedPos.getY() ),
				(float) ( entity.posZ - clickedPos.getZ() )
		);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.FACING);
	}
}
