package com.fanrende.myfirstmod.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks
{
	@ObjectHolder("myfirstmod:firstblock")
	public static FirstBlock FIRSTBLOCK;

	@ObjectHolder("myfirstmod:firstblock")
	public static TileEntityType<FirstBlockTile> FIRSTBLOCK_TILE;
}
