package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class EnergyPickaxe extends Item
{
	public EnergyPickaxe()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}

	private static final Random rand = new Random();

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

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return rand.nextDouble();
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return MathHelper.rgb(39.6f, 13.7f, 67.8f);
	}
}
