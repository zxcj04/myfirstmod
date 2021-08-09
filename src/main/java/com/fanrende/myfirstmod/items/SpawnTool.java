package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.gui.SpawnScreen;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SpawnTool extends Item
{
	public SpawnTool()
	{
		super(new Item.Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(
			Level worldIn, Player playerIn, InteractionHand handIn
	)
	{
		if (worldIn.isClientSide)
			SpawnScreen.open();

		return super.use(worldIn, playerIn, handIn);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.spawntool"));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}
}
