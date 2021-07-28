package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModSetup
{
	public ItemGroup itemGroup = new ItemGroup("myfirstmod")
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModBlocks.FIRSTBLOCK);
		}
	};

	public void init()
	{

	}
}
