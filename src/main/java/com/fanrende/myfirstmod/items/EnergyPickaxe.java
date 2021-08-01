package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.item.Item;

public class EnergyPickaxe extends Item
{
	public EnergyPickaxe()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}
}
