package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.MyFirstMod;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class FirstItem extends Item
{
	public FirstItem()
	{
		super(new Item.Properties()
				.maxStackSize(1)
				.group(MyFirstMod.setup.itemGroup)
		);

		setRegistryName("firstitem");
	}
}
