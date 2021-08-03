package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.tileentity.TileEntity;

public class MagicBlockTile extends TileEntity
{
	public MagicBlockTile()
	{
		super(Registration.MAGICBLOCK_TILE.get());
	}
}
