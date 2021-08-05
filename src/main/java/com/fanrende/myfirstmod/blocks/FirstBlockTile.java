package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.Config;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fanrende.myfirstmod.setup.Registration.FIRSTBLOCK_TILE;

public class FirstBlockTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private final ItemStackHandler item = this.createItemHandler();
	private final CustomEnergyStorage energy = this.createEnergyHandler();

	private final LazyOptional<IItemHandler> itemLazy = LazyOptional.of(() -> item);
	private final LazyOptional<IEnergyStorage> energyLazy = LazyOptional.of(() -> energy);

	private int generatorCounter = 0;

	public FirstBlockTile()
	{
		super(FIRSTBLOCK_TILE.get());
	}

	@Override
	public void tick()
	{
		if (world.isRemote)
			return;

		if (generatorCounter > 0)
		{
			generatorCounter--;

			energy.addEnergy(Config.FIRSTBLOCK_GENERATE.get());

			markDirty();
		}

		if (generatorCounter <= 0 && energy.getEnergyStored() < energy.getMaxEnergyStored())
		{
			ItemStack stack = item.getStackInSlot(0);

			if (stack.getItem() == Items.COBBLESTONE)
			{
				item.extractItem(0, 1, false);
				generatorCounter = Config.FIRSTBLOCK_TICKS.get();

				markDirty();
			}
		}

		BlockState state = world.getBlockState(pos);

		if (state.get(BlockStateProperties.POWERED) != generatorCounter > 0)
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, generatorCounter > 0),
					Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);

		sendOutEnergy();
	}

	private void sendOutEnergy()
	{
		AtomicInteger energyStored = new AtomicInteger(energy.getEnergyStored());

		if (energyStored.get() > 0)
		{
			for (Direction direction : Direction.values())
			{
				TileEntity tileEntity = world.getTileEntity(pos.offset(direction));

				if (tileEntity != null)
				{
					boolean doContinue = tileEntity.getCapability(CapabilityEnergy.ENERGY, direction).map(h ->
					{
						if (h.canReceive())
						{
							int received = h.receiveEnergy(Math.min(energyStored.get(),
									Config.FIRSTBLOCK_SEND.get()
							), false);

							energyStored.addAndGet(-received);

							( (CustomEnergyStorage) energy ).consumeEnergy(received);

							markDirty();

							return energyStored.get() > 0;
						}
						else
							return true;

					}).orElse(true);

					if (!doContinue)
						return;
				}
			}
		}
	}

	@Override
	public void read(CompoundNBT tag)
	{
		CompoundNBT invTag = tag.getCompound("inv");
		item.deserializeNBT(invTag);

		CompoundNBT energyTag = tag.getCompound("energy");
		energy.deserializeNBT(energyTag);

		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("inv", item.serializeNBT());
		tag.put("energy", energy.serializeNBT());

		return super.write(tag);
	}

	private ItemStackHandler createItemHandler()
	{
		return new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack)
			{
				return stack.getItem() == Items.COBBLESTONE;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
			{
				if (stack.getItem() != Items.COBBLESTONE)
					return stack;

				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	private CustomEnergyStorage createEnergyHandler()
	{
		return new CustomEnergyStorage(Config.FIRSTBLOCK_MAXPOWER.get(), 0)
		{
			@Override
			protected void onEnergyChanged()
			{
				markDirty();
			}
		};
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(
			@Nonnull Capability<T> cap, @Nullable Direction side
	)
	{
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemLazy.cast();
		else if (cap == CapabilityEnergy.ENERGY)
			return energyLazy.cast();

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
