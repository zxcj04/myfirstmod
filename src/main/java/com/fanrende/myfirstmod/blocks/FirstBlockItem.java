package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Config;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
			ItemStack stack, @Nullable CompoundNBT nbt
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
		return MathHelper.hsvToRGB(0.72F, 0.66F, 1.0F);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(
			ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn
	)
	{
		tooltip.add(new StringTextComponent("\u00A75" + "energy: \u00A77" + getEnergyStored(stack) + "\u00A75/\u00A77" + Config.FIRSTBLOCK_MAXPOWER.get()));
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("message.firstblock", Config.FIRSTBLOCK_GENERATE.get()));
		}
		else
		{
			tooltip.add(new TranslationTextComponent("message.pressshift"));
		}
	}
}
