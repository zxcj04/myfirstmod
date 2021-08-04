package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.fanrende.myfirstmod.setup.Registration.MAGICBLOCK;
import static com.fanrende.myfirstmod.setup.Registration.MAGICBLOCK_TILE;

public class MagicBlockTile extends TileEntity
{
	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(this::createItemHandler);

	public MagicBlockTile()
	{
		super(Registration.MAGICBLOCK_TILE.get());
	}

	private ItemStackHandler createItemHandler()
	{
		return new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
				markDirty();
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
			{
				ItemStack handlerStack = this.getStackInSlot(0);
				if(handlerStack.getCount() > 0)
					return stack;

				ItemStack stackInsert = ItemHandlerHelper.copyStackWithSize(stack, 1);

				if(stack.getCount() <= 1)
					stack = ItemStack.EMPTY;
				else
					stack.shrink(1);

				super.insertItem(slot, stackInsert, simulate);

				return stack;
			}
		};
	}

	@Override
	public void read(CompoundNBT tag)
	{
		CompoundNBT invTag = tag.getCompound("inv");
		itemHandler.ifPresent(h -> ( (INBTSerializable<CompoundNBT>) h ).deserializeNBT(invTag));

		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		itemHandler.ifPresent(h ->
		{
			CompoundNBT compound = ( (INBTSerializable<CompoundNBT>) h ).serializeNBT();
			tag.put("inv", compound);
		});

		return super.write(tag);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		SUpdateTileEntityPacket packet = new SUpdateTileEntityPacket(pos, 0, this.write(new CompoundNBT()));
		return packet;
	}

	@Override
	public void onDataPacket(
			NetworkManager net, SUpdateTileEntityPacket pkt
	)
	{
		System.out.println("in");
		this.read(pkt.getNbtCompound());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(
			@Nonnull Capability<T> cap, @Nullable Direction side
	)
	{
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();

		return super.getCapability(cap, side);
	}
}
