package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.items.FirstItem;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.fanrende.myfirstmod.blocks.ModBlocks.FIRSTBLOCK_TILE;

public class FirstBlockTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private ItemStackHandler handler;

	public FirstBlockTile()
	{
		super(FIRSTBLOCK_TILE);
	}

	@Override
	public void tick()
	{
	}

	@Override
	public void read(CompoundNBT tag)
	{
		CompoundNBT invTag = tag.getCompound("inv");
		getHandler().deserializeNBT(invTag);

		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		CompoundNBT compound = getHandler().serializeNBT();
		tag.put("inv", compound);

		return super.write(tag);
	}

	private ItemStackHandler getHandler()
	{
		if(handler == null)
			handler = new ItemStackHandler(3) {
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

		return handler;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(
			@Nonnull Capability<T> cap, @Nullable Direction side
	)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> (T) getHandler());
		}

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
