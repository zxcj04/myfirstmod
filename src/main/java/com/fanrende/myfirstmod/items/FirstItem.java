package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class FirstItem extends Item
{
	public FirstItem()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}
}
