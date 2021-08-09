package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class InfinityPearl extends Item
{
	public InfinityPearl()
	{
		super(new Item.Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP));
	}

	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		worldIn.playSound(
				(Player) null,
				playerIn.getX(),
				playerIn.getY(),
				playerIn.getZ(),
				SoundEvents.ENDER_PEARL_THROW,
				SoundSource.NEUTRAL,
				0.5F,
				0.4F / ( worldIn.random.nextFloat() * 0.4F + 0.8F )
		);
		playerIn.getCooldowns().addCooldown(this, 20);
		if (!worldIn.isClientSide)
		{
			ThrownEnderpearl enderpearlentity = new ThrownEnderpearl(worldIn, playerIn);
			enderpearlentity.setItem(itemstack);
			enderpearlentity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 1.0F);
			worldIn.addFreshEntity(enderpearlentity);
		}

		playerIn.awardStat(Stats.ITEM_USED.get(this));

		return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.infinitypearl"));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}
}
