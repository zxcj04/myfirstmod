package com.fanrende.myfirstmod.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>
{
	public CustomEnergyStorage(int capacity, int maxTransfer)
	{
		super(capacity, maxTransfer);
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public void addEnergy(int energy)
	{
		this.energy += energy;

		if (this.energy > getMaxEnergyStored())
		{
			this.energy = getMaxEnergyStored();
		}
	}

	public void consumeEnergy(int energy)
	{
		this.energy -= energy;

		if (this.energy < 0)
		{
			this.energy = 0;
		}
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("energy", getEnergyStored());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		setEnergy(nbt.getInt("energy"));
	}

	public static class Item extends CustomEnergyStorage
	{
		private final ItemStack stack;

		public Item(ItemStack stack, int capacity, int transfer)
		{
			super(capacity, transfer);
			this.stack = stack;
			setEnergy(this.stack.getOrCreateTag().getInt("energy"));
		}

		@Override
		public void setEnergy(int energy)
		{
			super.setEnergy(energy);

			this.stack.getOrCreateTag().putInt("energy", getEnergyStored());
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			int energy = super.receiveEnergy(maxReceive, simulate);
			if (!simulate)
			{
				this.stack.getOrCreateTag().putInt("energy", getEnergyStored());
			}
			return energy;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
			int energy = super.extractEnergy(maxExtract, simulate);
			if (!simulate)
			{
				this.stack.getOrCreateTag().putInt("energy", getEnergyStored());
			}
			return energy;
		}

		public static class Provider implements ICapabilityProvider
		{
			private final ItemStack stack;
			private final int capacity;
			private final int transfer;

			public Provider(ItemStack stack, int capacity, int transfer)
			{
				this.stack = stack;
				this.capacity = capacity;
				this.transfer = transfer;
			}

			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
			{
				return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() ->
				{
					return new Item(this.stack, this.capacity, this.transfer);
				}).cast() : LazyOptional.empty();
			}
		}
	}

	public static class BlockItem extends CustomEnergyStorage
	{
		private final ItemStack stack;

		public BlockItem(ItemStack stack, int capacity, int transfer)
		{
			super(capacity, transfer);
			this.stack = stack;
			setEnergy(this.stack.getOrCreateChildTag("BlockEntityTag").getCompound("energy").getInt("energy"));
		}

		@Override
		public void setEnergy(int energy)
		{
			super.setEnergy(energy);

			this.stack.getOrCreateChildTag("BlockEntityTag").getCompound("energy").putInt("energy", getEnergyStored());
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			int energy = super.receiveEnergy(maxReceive, simulate);
			if (!simulate)
			{
				this.stack.getOrCreateChildTag("BlockEntityTag").getCompound("energy").putInt("energy", getEnergyStored());
			}
			return energy;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
			int energy = super.extractEnergy(maxExtract, simulate);
			if (!simulate)
			{
				this.stack.getOrCreateChildTag("BlockEntityTag").getCompound("energy").putInt("energy", getEnergyStored());
			}
			return energy;
		}

		public static class Provider implements ICapabilityProvider
		{
			private final ItemStack stack;
			private final int capacity;
			private final int transfer;

			public Provider(ItemStack stack, int capacity, int transfer)
			{
				this.stack = stack;
				this.capacity = capacity;
				this.transfer = transfer;
			}

			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
			{
				return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() ->
				{
					return new BlockItem(this.stack, this.capacity, this.transfer);
				}).cast() : LazyOptional.empty();
			}
		}
	}
}
