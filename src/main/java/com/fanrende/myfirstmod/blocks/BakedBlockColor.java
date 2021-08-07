package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BakedBlockColor implements IBlockColor
{
	@Override
	public int getColor(
			BlockState state, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tint
	)
	{
		if (world != null)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof BakedBlockTile)
			{
				BakedBlockTile baked = (BakedBlockTile) tileEntity;
				BlockState mimic = baked.getMimic();

				if (mimic != null)
					return Minecraft.getInstance().getBlockColors().getColor(mimic, world, pos, tint);
			}
		}

		return -1;
	}
}
