package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static com.fanrende.myfirstmod.setup.Registration.FIRSTBLOCK;
import static com.fanrende.myfirstmod.setup.Registration.FIRSTBLOCK_CONTAINER;

public class FirstBlockContainer extends Container
{
	private final TileEntity tileEntity;
	private final IItemHandler playerInventory;

	public FirstBlockContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory)
	{
		super(FIRSTBLOCK_CONTAINER.get(), windowId);

		this.tileEntity = world.getBlockEntity(pos);

		this.playerInventory = new InvWrapper(playerInventory);

		this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
		{
			addSlot(new SlotItemHandler(h, 0, 82, 24));
		});

		layoutPlayerInventorySlots(10, 70);

		trackPower();
	}

	private void trackPower()
	{
		// because of 64 bits and 32 bits difference between server and client on dedicated server
		// we need two "int" to store and transfer

		addDataSlot(new IntReferenceHolder()
		{
			@Override
			public int get()
			{
				return getEnergy() & 0xffff;
			}

			@Override
			public void set(int value)
			{
				tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h ->
				{
					int energyStored = h.getEnergyStored() & 0xffff0000;
					( (CustomEnergyStorage) h ).setEnergy(energyStored + ( value & 0xffff ));
				});
			}
		});

		addDataSlot(new IntReferenceHolder()
		{
			@Override
			public int get()
			{
				return ( getEnergy() >> 16 ) & 0xffff;
			}

			@Override
			public void set(int value)
			{
				tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h ->
				{
					int energyStored = h.getEnergyStored() & 0x0000ffff;
					( (CustomEnergyStorage) h ).setEnergy(energyStored | ( value << 16 ));
				});
			}
		});
	}

	public int getEnergy()
	{
		return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}

	@Override
	public boolean stillValid(PlayerEntity playerEntity)
	{
		return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
				playerEntity,
				FIRSTBLOCK.get()
		);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem())
		{
			ItemStack stack = slot.getItem();
			itemstack = stack.copy();
			if (index == 0)
			{
				if (!this.moveItemStackTo(stack, 1, 37, true))
				{
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(stack, itemstack);
			}
			else
			{
				if (stack.getItem() == Items.COBBLESTONE)
				{
					if (!this.moveItemStackTo(stack, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index < 28)
				{
					if (!this.moveItemStackTo(stack, 28, 37, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index < 37 && !this.moveItemStackTo(stack, 1, 28, false))
				{
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty())
			{
				slot.set(ItemStack.EMPTY);
			}
			else
			{
				slot.setChanged();
			}

			if (stack.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stack);
		}

		return itemstack;
	}

	private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx)
	{
		for (int i = 0; i < amount; i++)
		{
			addSlot(new SlotItemHandler(handler, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy)
	{
		for (int j = 0; j < verAmount; j++)
		{
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	private void layoutPlayerInventorySlots(int leftCol, int topRow)
	{
		// Player inventory
		addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

		// Hotbar
		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}
}
