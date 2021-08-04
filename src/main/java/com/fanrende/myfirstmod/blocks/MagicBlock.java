package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class MagicBlock extends Block
{
	private static final VoxelShape RENDER_SHAPE = VoxelShapes.empty();

	public MagicBlock()
	{
		super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.0f));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(
			BlockState state, IBlockReader world
	)
	{
		return new MagicBlockTile();
	}

	@Override
	public VoxelShape getRenderShape(
			BlockState state, IBlockReader worldIn, BlockPos pos
	)
	{
		return RENDER_SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public ActionResultType onBlockActivated(
			BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit
	)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
			{
				ItemStack stack = player.getHeldItem(hand);

				if (stack.isEmpty())
					stack = h.extractItem(0, h.getStackInSlot(0).getCount(), false);
				else
					stack = h.insertItem(0, stack, false);

				player.setHeldItem(hand, stack);
			});
		}

		return ActionResultType.SUCCESS;
	}
}
