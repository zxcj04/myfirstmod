package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BakedBlockTile extends BlockEntity
{
	public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

	private BlockState mimic;

	public BakedBlockTile(BlockPos pos, BlockState state)
	{
		super(Registration.BAKEDBLOCK_TILE.get(), pos, state);
	}

	public void setMimic(BlockState mimic)
	{
		if (mimic.getBlock() == Registration.BAKEDBLOCK.get())
			return;

		this.mimic = mimic;
		setChanged();
		level.sendBlockUpdated(
				worldPosition,
				getBlockState(),
				getBlockState(),
				Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
		);
	}

	public BlockState getMimic()
	{
		return mimic;
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tag = super.getUpdateTag();
		writeMimic(tag);

		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		// This is actually the default but placed here so you
		// know this is the place to potentially have a lighter read() that only
		// considers things needed client-side
		load(tag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		BlockState oldMimic = mimic;
		CompoundTag tag = pkt.getTag();
		handleUpdateTag(tag);

		if (!Objects.equals(oldMimic, mimic))
		{
			ModelDataManager.requestModelDataRefresh(this);
			level.sendBlockUpdated(
					worldPosition,
					getBlockState(),
					getBlockState(),
					Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
			);
		}
	}

	@Nonnull
	@Override
	public IModelData getModelData()
	{
		return new ModelDataMap.Builder().withInitial(MIMIC, mimic).build();
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		readMimic(compound);
	}

	private void readMimic(CompoundTag tag)
	{
		if (tag.contains("mimic"))
			mimic = NbtUtils.readBlockState(tag.getCompound("mimic"));
	}

	@Override
	public CompoundTag save(CompoundTag compound)
	{
		writeMimic(compound);
		return super.save(compound);
	}

	private void writeMimic(CompoundTag tag)
	{
		if (mimic != null)
			tag.put("mimic", NbtUtils.writeBlockState(mimic));
	}
}
