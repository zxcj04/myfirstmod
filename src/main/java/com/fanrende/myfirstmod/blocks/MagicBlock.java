package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

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
}
