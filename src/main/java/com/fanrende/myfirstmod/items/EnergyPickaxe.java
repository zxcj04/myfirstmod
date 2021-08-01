package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class EnergyPickaxe extends Item
{
	public EnergyPickaxe()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		return 8.0f;
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn)
	{
		if(blockIn.getHarvestTool() == ToolType.PICKAXE)
			return true;
		else
			return false;
	}
}
