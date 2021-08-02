package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.Config;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class FirstBlockItem extends BlockItem
{
	public FirstBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	private int getEnergyStored(ItemStack stack)
	{
		if (!stack.getOrCreateTag().getBoolean("checked"))
		{
			int energyStored = stack.getOrCreateChildTag("BlockEntityTag").getCompound("energy").getInt("energy");
			stack.getCapability(CapabilityEnergy.ENERGY)
					.ifPresent(h -> ( (CustomEnergyStorage.Item) h ).setEnergy(energyStored));

			stack.getOrCreateTag().putBoolean("checked", true);
		}
		else
		{
			stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h ->
			{
				stack.getOrCreateChildTag("BlockEntityTag").getCompound("energy").putInt("energy", h.getEnergyStored());
			});
		}

		return stack.getCapability(CapabilityEnergy.ENERGY).map(h -> h.getEnergyStored()).orElse(0);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(
			ItemStack stack, @Nullable CompoundNBT nbt
	)
	{
		return new CustomEnergyStorage.Item.Provider(stack, Config.FIRSTBLOCK_MAXPOWER.get(), 0);
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
}
