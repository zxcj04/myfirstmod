package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MagicBlockTile extends TileEntity
{
	private final ItemStackHandler item = this.createItemHandler();
	private final LazyOptional<IItemHandler> itemLazy = LazyOptional.of(() -> item);

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
				world.notifyBlockUpdate(
						pos,
						getBlockState(),
						getBlockState(),
						Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
				);
				markDirty();
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
			{
				ItemStack handlerStack = this.getStackInSlot(0);
				if (handlerStack.getCount() > 0)
					return stack;

				ItemStack stackInsert = ItemHandlerHelper.copyStackWithSize(stack, 1);

				if (stack.getCount() <= 1)
					stack = ItemStack.EMPTY;
				else
					stack.shrink(1);

				super.insertItem(slot, stackInsert, simulate);

				return stack;
			}
		};
	}

	@Override
	public void read(BlockState state, CompoundNBT tag)
	{
		CompoundNBT invTag = tag.getCompound("inv");
		item.deserializeNBT(invTag);

		super.read(state, tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("inv", item.serializeNBT());

		return super.write(tag);
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tag = super.getUpdateTag();
		return this.write(tag);
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		read(state, tag);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		SUpdateTileEntityPacket packet = new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
		return packet;
	}

	@Override
	public void onDataPacket(
			NetworkManager net, SUpdateTileEntityPacket pkt
	)
	{
		handleUpdateTag(getBlockState(), pkt.getNbtCompound());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(
			@Nonnull Capability<T> cap, @Nullable Direction side
	)
	{
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemLazy.cast();

		return super.getCapability(cap, side);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(getPos(), getPos().add(1, 3, 1));
	}
}
