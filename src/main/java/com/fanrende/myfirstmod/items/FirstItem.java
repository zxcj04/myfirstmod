package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FirstItem extends Item
{
	public FirstItem()
	{
		super(new Item.Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.firstitem"));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}
}
