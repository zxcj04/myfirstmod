package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BakedBlock extends Block
{
	private final VoxelShape shape = VoxelShapes.create(.2, .2, .2, .8, .8, .8);

	public BakedBlock()
	{
		super(
			Properties.create(Material.WOOD)
				.sound(SoundType.WOOD)
				.hardnessAndResistance(1.0f)
		);
	}

	@Override
	public VoxelShape getShape(
			BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context
	)
	{
		return shape;
	}
}
