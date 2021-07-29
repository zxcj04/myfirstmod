package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.fanrende.myfirstmod.blocks.ModBlocks.FIRSTBLOCK_TILE;

public class FirstBlockTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(this::createItemHandler);
	private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(this::createEnergyHandler);

	private int generatorCounter = 0;

	public FirstBlockTile()
	{
		super(FIRSTBLOCK_TILE);
	}

	@Override
	public void tick()
	{
		if (generatorCounter > 0)
		{
			generatorCounter--;

			if (generatorCounter <= 0)
			{
				energyHandler.ifPresent(h -> ((CustomEnergyStorage) h).addEnergy(1000));
			}
		}
		else
		{
			energyHandler.ifPresent( eh ->
			{
				if(eh.getEnergyStored() < eh.getMaxEnergyStored())
				{
					itemHandler.ifPresent(h ->
					{
						ItemStack stack = h.getStackInSlot(0);

						if (stack.getItem() == Items.COBBLESTONE)
						{
							h.extractItem(0, 1, false);
							generatorCounter = 20;
						}
					});
				}
			});
		}
	}

	@Override
	public void read(CompoundNBT tag)
	{
		CompoundNBT invTag = tag.getCompound("inv");
		itemHandler.ifPresent(h -> (( INBTSerializable<CompoundNBT> ) h).deserializeNBT(invTag));

		CompoundNBT energyTag = tag.getCompound("energy");
		energyHandler.ifPresent(h -> (( INBTSerializable<CompoundNBT> ) h).deserializeNBT(energyTag));

		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		itemHandler.ifPresent(h -> {
			CompoundNBT compound = (( INBTSerializable<CompoundNBT> ) h).serializeNBT();
			tag.put("inv", compound);
		});

		energyHandler.ifPresent(h -> {
			CompoundNBT compound = (( INBTSerializable<CompoundNBT>) h).serializeNBT();
			tag.put("energy", compound);
		});

		return super.write(tag);
	}

	private ItemStackHandler createItemHandler()
	{
		return new ItemStackHandler(1){
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack)
			{
				return stack.getItem() == Items.COBBLESTONE;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
			{
				if(stack.getItem() != Items.COBBLESTONE)
					return stack;

				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	private CustomEnergyStorage createEnergyHandler()
	{
		return new CustomEnergyStorage(10000, 0);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(
			@Nonnull Capability<T> cap, @Nullable Direction side
	)
	{
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		else if (cap == CapabilityEnergy.ENERGY)
			return energyHandler.cast();

		return super.getCapability(cap, side);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent(getType().getRegistryName().getPath());
	}

	@Nullable
	@Override
	public Container createMenu(
			int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity
	)
	{
		return new FirstBlockContainer(windowId, world, pos, playerInventory);
	}
}
