package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Config;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class FirstBlockItem extends BlockItem
{
	public FirstBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	private int getEnergyStored(ItemStack stack)
	{
		return stack.getCapability(CapabilityEnergy.ENERGY).map(h -> h.getEnergyStored()).orElse(0);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(
			ItemStack stack, @Nullable CompoundTag nbt
	)
	{
		return new CustomEnergyStorage.BlockItem.Provider(stack, Config.FIRSTBLOCK_MAXPOWER.get(), 0);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		int energyStored = getEnergyStored(stack);

		return (double) 1 - ( (double) energyStored / (double) Config.FIRSTBLOCK_MAXPOWER.get() );
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return Mth.hsvToRgb(0.72F, 0.66F, 1.0F);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		tooltip.add(new TextComponent("\u00A75" + "energy: \u00A77" + getEnergyStored(stack) + "\u00A75/\u00A77" + Config.FIRSTBLOCK_MAXPOWER.get()));
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.firstblock", Config.FIRSTBLOCK_GENERATE.get()));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}
}
