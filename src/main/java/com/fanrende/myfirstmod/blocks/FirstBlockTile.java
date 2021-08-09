package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Config;
import com.fanrende.myfirstmod.setup.Registration;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class FirstBlockTile extends BlockEntity implements MenuProvider
{
	private final ItemStackHandler item = this.createItemHandler();
	private final CustomEnergyStorage energy = this.createEnergyHandler();

	private final LazyOptional<IItemHandler> itemLazy = LazyOptional.of(() -> item);
	private final LazyOptional<IEnergyStorage> energyLazy = LazyOptional.of(() -> energy);

	private int generatorCounter = 0;

	public FirstBlockTile(BlockPos pos, BlockState state)
	{
		super(Registration.FIRSTBLOCK_TILE.get(), pos, state);
	}

	public void tickServer()
	{
		if (generatorCounter > 0)
		{
			generatorCounter--;

			energy.addEnergy(Config.FIRSTBLOCK_GENERATE.get());

			setChanged();
		}

		if (generatorCounter <= 0 && energy.getEnergyStored() < energy.getMaxEnergyStored())
		{
			ItemStack stack = item.getStackInSlot(0);

			if (stack.getItem() == Items.COBBLESTONE)
			{
				item.extractItem(0, 1, false);
				generatorCounter = Config.FIRSTBLOCK_TICKS.get();

				setChanged();
			}
		}

		BlockState state = level.getBlockState(worldPosition);

		if (state.getValue(BlockStateProperties.POWERED) != generatorCounter > 0)
			level.setBlock(worldPosition,
					state.setValue(BlockStateProperties.POWERED, generatorCounter > 0),
					Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
			);

		sendOutEnergy();
	}

	private void sendOutEnergy()
	{
		AtomicInteger energyStored = new AtomicInteger(energy.getEnergyStored());

		if (energyStored.get() > 0)
		{
			for (Direction direction : Direction.values())
			{
				BlockEntity tileEntity = level.getBlockEntity(worldPosition.relative(direction));

				if (tileEntity != null)
				{
					boolean doContinue = tileEntity.getCapability(CapabilityEnergy.ENERGY, direction).map(h ->
					{
						if (h.canReceive())
						{
							int received = h.receiveEnergy(Math.min(energyStored.get(), Config.FIRSTBLOCK_SEND.get()),
									false
							);

							energyStored.addAndGet(-received);

							( (CustomEnergyStorage) energy ).consumeEnergy(received);

							setChanged();

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
	public void load(CompoundTag tag)
	{
		CompoundTag invTag = tag.getCompound("inv");
		item.deserializeNBT(invTag);

		Tag energyTag = tag.get("energy");
		energy.deserializeNBT(energyTag);

		super.load(tag);
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		tag.put("inv", item.serializeNBT());
		tag.put("energy", energy.serializeNBT());

		return super.save(tag);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tag = super.getUpdateTag();
		save(tag);

		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		super.handleUpdateTag(tag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(
			Connection net, ClientboundBlockEntityDataPacket pkt
	)
	{
		handleUpdateTag(pkt.getTag());
	}

	private ItemStackHandler createItemHandler()
	{
		return new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
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
				setChanged();
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
	public Component getDisplayName()
	{
		return new TextComponent(getType().getRegistryName().getPath());
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(
			int windowId, Inventory playerInventory, Player playerEntity
	)
	{
		return new FirstBlockContainer(windowId, level, worldPosition, playerInventory);
	}
}
