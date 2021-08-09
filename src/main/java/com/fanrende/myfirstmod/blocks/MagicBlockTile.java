package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MagicBlockTile extends BlockEntity
{
	private final ItemStackHandler item = this.createItemHandler();
	private final LazyOptional<IItemHandler> itemLazy = LazyOptional.of(() -> item);

	public MagicBlockTile(BlockPos pos, BlockState state)
	{
		super(Registration.MAGICBLOCK_TILE.get(), pos, state);
	}

	private ItemStackHandler createItemHandler()
	{
		return new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				level.sendBlockUpdated(worldPosition,
						getBlockState(),
						getBlockState(),
						Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
				);
				setChanged();
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
	public void load(CompoundTag tag)
	{
		CompoundTag invTag = tag.getCompound("inv");
		item.deserializeNBT(invTag);

		super.load(tag);
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		tag.put("inv", item.serializeNBT());

		return super.save(tag);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tag = super.getUpdateTag();
		return this.save(tag);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		load(tag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		ClientboundBlockEntityDataPacket packet = new ClientboundBlockEntityDataPacket(worldPosition,
				0,
				getUpdateTag()
		);
		return packet;
	}

	@Override
	public void onDataPacket(
			Connection net, ClientboundBlockEntityDataPacket pkt
	)
	{
		handleUpdateTag(pkt.getTag());
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
	public AABB getRenderBoundingBox()
	{
		return new AABB(getBlockPos(), getBlockPos().offset(1, 3, 1));
	}
}
