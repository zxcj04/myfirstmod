package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.gui.SpawnScreen;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SpawnTool extends Item
{
	public SpawnTool()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(
			World worldIn, PlayerEntity playerIn, Hand handIn
	)
	{
		SpawnScreen.open();

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
