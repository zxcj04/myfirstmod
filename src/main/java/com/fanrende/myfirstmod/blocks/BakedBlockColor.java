package com.fanrende.myfirstmod.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BakedBlockColor implements BlockColor
{
	@Override
	public int getColor(
			BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tint
	)
	{
		if (world != null)
		{
			BlockEntity tileEntity = world.getBlockEntity(pos);
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
